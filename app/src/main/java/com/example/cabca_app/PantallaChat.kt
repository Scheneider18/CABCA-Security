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

    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()

    private lateinit var newRecycler: RecyclerView
    private lateinit var addChat: Button
    private var imageId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_chat)

        addChat = findViewById(R.id.btnAddChat)

        imageId = R.drawable.logogrande

        auth = Firebase.auth

        addChat!!.setOnClickListener(this)

        newRecycler = findViewById(R.id.recycler)
        newRecycler.layoutManager = LinearLayoutManager(this)
        newRecycler.setHasFixedSize(true)


        getUserData()
    }

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

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnAddChat ->
                addChats()
        }
    }

    private fun addChats(){
        val intent = Intent(this, AllChats::class.java)
        startActivity(intent)
    }
}