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

    //Crear las variables para enlazarlos elementos
    private var iButCamara: ImageButton? = null
    private var iButChat: ImageButton? = null
    private var iButEmergencia: ImageButton? = null
    private var iButQR: ImageButton? = null
    private var btnSignOut: Button? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)

        //Inicializar los elementos
        iButCamara = findViewById<ImageButton>(R.id.iButCamara)
        iButChat = findViewById<ImageButton>(R.id.iButChat)
        iButEmergencia = findViewById<ImageButton>(R.id.iButEmergencia)
        iButQR = findViewById<ImageButton>(R.id.iButQR)
        btnSignOut = findViewById<Button>(R.id.btnSignOut)

        // Initialize Firebase Auth
        auth = Firebase.auth

        //Asignar el metodo OnClick a los botones
        iButCamara!!.setOnClickListener(this)
        iButChat!!.setOnClickListener(this)
        iButEmergencia!!.setOnClickListener(this)
        iButQR!!.setOnClickListener(this)
        btnSignOut!!.setOnClickListener(this)

    }

    //Sobreescribir la funcion onClick para que por cada item diferente
    //Realice cierta actividad o función
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

    //Función para iniciar la activity del módulo de Cámaras
    private fun mainCamara(){
        val intent = Intent(this, PantallaCamara::class.java).apply {  }
        startActivity(intent)
    }

    //Función para iniciar la activity del módulo de Chats
    private fun mainChat(){
        val intent = Intent(this, PantallaChat::class.java).apply {  }
        startActivity(intent)
    }

    //Función para iniciar la activity del módulo de Botón de emergencias
    private fun mainEmergencia(){
        val intent = Intent(this, PantallaEmergencia::class.java).apply {  }
        startActivity(intent)
    }

    //Función para iniciar la activity del módulo de Control de acceso mediante código QR
    private fun mainQR(){
        val intent = Intent(this, PantallaQR::class.java).apply {  }
        startActivity(intent)
    }

    //Función para cerrar la sesión del usuario
    private  fun signOut(){
        auth.signOut()
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
    }
}