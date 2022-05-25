package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PantallaChat : AppCompatActivity(), View.OnClickListener {
    //Crear las variables para enlazarlos elementos
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    private lateinit var newRecycler: RecyclerView
    private lateinit var addChat: Button
    private var imageId: Int = 0
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_chat)
        //Inicializar los elementos
        addChat = findViewById(R.id.btnAddChat)
        newRecycler = findViewById(R.id.recycler)
        newRecycler.layoutManager = LinearLayoutManager(this)
        newRecycler.setHasFixedSize(true)
        imageId = R.drawable.logogrande
        //Inicializar Firebase Auth
        auth = Firebase.auth
        //Asignar el metodo OnClick a los botones
        addChat!!.setOnClickListener(this)
        //Llamado a la función para obtener datos
        getUserData()
    }
    //Función para obtener los datos del usuario, chats recientes y enlistarlos en un adapter
    private fun getUserData() {
        val user = auth.currentUser
        if (user != null){
            val userRef = db.collection("usersc").document(user.email as String)
            userRef.collection("chats")
                .get()
                .addOnSuccessListener { chats ->
                    //Toast.makeText(this, "Datos obtenidos ${chats}",Toast.LENGTH_LONG).show()
                    val listChats = chats.toObjects(Chat::class.java)

                    var adapter = AdaptadorChat(listChats)
                    newRecycler.adapter = adapter
                    adapter.setOnItemClickListener(object : AdaptadorChat.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            chatSelected(listChats[position])
                        }
                    })
                }
            userRef.collection("chats")
                .addSnapshotListener { chats, error ->
                    if(error == null){
                        chats?.let {
                            //Toast.makeText(this, "Datos obtenidos ${chats}",Toast.LENGTH_LONG).show()
                            val listChats = it.toObjects(Chat::class.java)

                            var adapter = AdaptadorChat(listChats)
                            newRecycler.adapter = adapter
                            adapter.setOnItemClickListener(object : AdaptadorChat.onItemClickListener{
                                override fun onItemClick(position: Int) {
                                    chatSelected(listChats[position])
                                }
                            })
                        }
                    }
                }


        }else{
            Toast.makeText(this, "Error con conexion", Toast.LENGTH_SHORT).show()
        }
    }
    //Función que nos dirige a los mensajes con el chat seleccionado enviando datos del usuario actual
    private fun chatSelected(chat: Chat){
        val user = auth.currentUser
        val intent = Intent(this@PantallaChat, ChatIndv::class.java)
        intent.putExtra("direccion", chat.name)
        intent.putExtra("id", chat.id)
        intent.putExtra("user", user?.email)
        //intent.putExtra("mensaje", chat.users as String)
        intent.putExtra("imagen", chat.image)
        startActivity(intent)
    }
    //Sobreescribir la funcion onClick para que por cada item diferente
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnAddChat ->
                addChats()
        }
    }
    //Función para añadir un chat
    private fun addChats(){
        val intent = Intent(this, AllChats::class.java)
        startActivity(intent)
    }
}