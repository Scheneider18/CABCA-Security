package com.example.cabca_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatIndv : AppCompatActivity(), View.OnClickListener {
    //Crear las variables para enlazarlos elementos
    private var chatid = ""
    private var user = ""
    private var direccion = ""
    private var imageId = 0
    private lateinit var newRecycler: RecyclerView
    private lateinit var eTMess: EditText
    private lateinit var send: Button
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_indv)
        //Inicializar los elementos
        val txViDirecChat: TextView = findViewById(R.id.txViewDireChat)
        val imViAv: ImageView = findViewById(R.id.imViAvChat)
        eTMess = findViewById(R.id.eTMensaje)
        send = findViewById(R.id.buttonSend)
        newRecycler = findViewById(R.id.recyclerMess)
        newRecycler.layoutManager = LinearLayoutManager(this)
        newRecycler.setHasFixedSize(true)
        val bundle: Bundle? = intent.extras
        chatid = bundle?.getString("id") as String
        direccion = bundle?.getString("direccion") as String
        user = bundle?.getString("user") as String
        imageId = bundle?.getInt("image")
        txViDirecChat.text = direccion
        imViAv.setImageResource(imageId)
        //Inicializar Firebase Auth
        auth = Firebase.auth
        //Asignar el metodo OnClick a los botones
        send!!.setOnClickListener(this)
        //Función para verificar que se cargaron correctamente los mensajes
        if(chatid.isEmpty()){
            Toast.makeText(this,"Problemas al cargar mensajes.", Toast.LENGTH_SHORT).show()
        }else{
            //Llamdado de la función para cargar los mensajes del chat
            getUserMessage()
        }
    }
    //Función para obtener los mensajes en tiempo real del chat
    private fun getUserMessage() {
        val user = auth.currentUser
        if (user != null){
            db.collection("chats").document(chatid).collection("messages").orderBy("dob", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { messages ->
                    val listMessages = messages.toObjects(Message::class.java)
                    var adapter = AdaptadorMessage(listMessages, user.email as String)
                    newRecycler.adapter = adapter
                }

            db.collection("chats").document(chatid).collection("messages").orderBy("dob", Query.Direction.ASCENDING)
                .addSnapshotListener { messages, error ->
                    if(error == null){
                        messages?.let {
                            val listMessages = it.toObjects(Message::class.java)
                            var adapter = AdaptadorMessage(listMessages, user.email as String)
                            newRecycler.adapter = adapter
                        }
                    }
                }
        }else{
            Toast.makeText(this, "Error con conexion", Toast.LENGTH_SHORT).show()
        }
    }
    //Función para enviar el mensaje y guardarlo en la base de datos
    private fun sendMessage(){
        val message = Message(
            message = eTMess.text.toString(),
            from = user
        )

        db.collection("chats").document(chatid).collection("messages").document().set(message)

        eTMess.setText("")
    }
    //Sobreescribir la funcion onClick para que por cada item diferente
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buttonSend ->
                sendMessage()
        }
    }
}