package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PantallaPrincipal : AppCompatActivity(), View.OnClickListener {

    private var iButCamara: ImageButton? = null
    private var iButChat: ImageButton? = null
    private var iButEmergencia: ImageButton? = null
    private var iButQR: ImageButton? = null
    private var btnSignOut: Button? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)

        iButCamara = findViewById<ImageButton>(R.id.iButCamara)
        iButChat = findViewById<ImageButton>(R.id.iButChat)
        iButEmergencia = findViewById<ImageButton>(R.id.iButEmergencia)
        iButQR = findViewById<ImageButton>(R.id.iButQR)
        btnSignOut = findViewById<Button>(R.id.btnSignOut)

        // Initialize Firebase Auth
        auth = Firebase.auth

        iButCamara!!.setOnClickListener(this)
        iButChat!!.setOnClickListener(this)
        iButEmergencia!!.setOnClickListener(this)
        iButQR!!.setOnClickListener(this)
        btnSignOut!!.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.iButCamara ->
                mainCamara()
            R.id.iButChat ->
                mainChat()
            R.id.iButEmergencia ->
                mainEmergencia()
            R.id.iButQR ->
                mainQR()
            R.id.btnSignOut ->
                signOut()
        }
    }

    private fun mainCamara(){
        val intent = Intent(this, PantallaCamara::class.java).apply {  }
        startActivity(intent)
    }

    private fun mainChat(){
        val intent = Intent(this, PantallaChat::class.java).apply {  }
        startActivity(intent)
    }

    private fun mainEmergencia(){
        val intent = Intent(this, PantallaEmergencia::class.java).apply {  }
        startActivity(intent)
    }

    private fun mainQR(){
        val intent = Intent(this, PantallaQR::class.java).apply {  }
        startActivity(intent)
    }

    private  fun signOut(){
        auth.signOut()
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
    }
}