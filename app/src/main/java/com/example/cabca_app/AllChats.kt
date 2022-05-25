package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AllChats : AppCompatActivity() {
    //Crear las variables para enlazarlos elementos
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var newRecycler: RecyclerView
    private var imageId: Int = 0
    private var direc = ""
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_chats)
        //Inicializar los elementos
        imageId = R.drawable.logogrande
        newRecycler = findViewById(R.id.recyclerContacts)
        newRecycler.layoutManager = LinearLayoutManager(this)
        newRecycler.setHasFixedSize(true)
        //Inicializar Firebase Auth
        auth = Firebase.auth
        //Llamado a la función para obtener los contactos disponibles
        getContactsData()
    }
    //Función para obtener la información de los contactos disponibles y enlistarlos en un adapter
    private fun getContactsData() {
        val user = auth.currentUser
        db.collection("contacts").document(user?.email.toString())
            .get()
            .addOnSuccessListener { contact ->
                //Toast.makeText(this, "Se obtuvo dato.", Toast.LENGTH_SHORT).show()
                direc = contact.data?.get("name") as String
            }
        db.collection("contacts")
                .get()
                .addOnSuccessListener { contacts ->
                    //Toast.makeText(this, "Datos obtenidos ${contacts.documents}",Toast.LENGTH_SHORT).show()

                    val listChats = contacts.toObjects(Contactos::class.java)
                    var adapter = AdaptadorContacto(listChats)
                    newRecycler.adapter = adapter
                    adapter.setOnItemClickListener(object :  AdaptadorContacto.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            //Toast.makeText(this@AllChats, "Datos obtenidos ${listChats[position].correo}",Toast.LENGTH_SHORT).show()
                            newChat(listChats[position].correo, listChats[position].name)
                        }
                    })
            }

    }
    //Función para crear un chat nuevo con la información del contacto seleccionado y almacenarlo en la base de datos
    private fun newChat(correo: String, direccion: String){
        val currentUser = auth.currentUser
        if (currentUser != null){
            if (currentUser.email.equals(correo)){
                Toast.makeText(this,"Seleccione otro chat que no sea con usted mismo",Toast.LENGTH_SHORT).show()
            }else{
                val chatId = UUID.randomUUID().toString()
                val otherUser = correo
                val otherDirec = direccion
                val users = listOf(currentUser.email as String, otherUser)


                val chat = Chat(
                    image = imageId,
                    id = chatId,
                    name = "Chat con $otherDirec",
                    users = users,
                )

                val chat2 = Chat(
                    image = imageId,
                    id = chatId,
                    name = "Chat con $direc",
                    users = users,
                )

                db.collection("chats").document(chatId).set(chat)
                db.collection("usersc").document(currentUser.email as String).collection("chats").document(chatId).set(chat)
                db.collection("usersc").document(otherUser).collection("chats").document(chatId).set(chat2)

                val intent = Intent(this, ChatIndv::class.java)
                intent.putExtra("user", currentUser.email as String)
                intent.putExtra("direccion", chat.name)
                intent.putExtra("id", chat.id)
                //intent.putExtra("mensaje", chat.users as String)
                intent.putExtra("imagen", chat.image)
                startActivity(intent)
            }

        }

    }
}