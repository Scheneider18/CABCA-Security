package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InicioSesion : AppCompatActivity(), View.OnClickListener {

    private var buttonIS: Button? = null
    private var buttonR: Button? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonIS = findViewById<Button>(R.id.buttonIS)
        buttonR = findViewById<Button>(R.id.buttonR)

        // Initialize Firebase Auth
        auth = Firebase.auth

        buttonIS!!.setOnClickListener(this)
        buttonR!!.setOnClickListener(this)

    }

    fun nextPage(){
        val intent = Intent(this, InicioSesion2::class.java).apply {  }
        startActivity(intent)
        /*Linea para cerrar activity y que no consuma recursos*/
        //finish()
    }
    fun nextPageR(){
        val intent = Intent(this, PantallaRegistro::class.java).apply {  }
        startActivity(intent)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.buttonIS ->
                nextPage()
            R.id.buttonR ->
                nextPageR()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                reload()
            }else{
                check()
            }
        }
    }

    private fun check(){
        val intent = Intent(this, CheckEmail::class.java).apply {  }
        startActivity(intent)
        finish()
    }

    private fun reload(){
        val intent = Intent(this, PantallaPrincipal::class.java).apply {  }
        startActivity(intent)
        finish()
    }
}