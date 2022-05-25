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
    //Variables para el uso de elementos visuales y lógicos
    private var buttonIS: Button? = null
    private var buttonR: Button? = null
    private lateinit var auth: FirebaseAuth
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Inicializar los elementos
        buttonIS = findViewById<Button>(R.id.buttonIS)
        buttonR = findViewById<Button>(R.id.buttonR)
        //Inicializar Firebase Auth
        auth = Firebase.auth
        //Asignar el metodo OnClick a los botones
        buttonIS!!.setOnClickListener(this)
        buttonR!!.setOnClickListener(this)

    }
    //Función creadora de la activity dirigida al inicio de sesión y asignada al botón "Iniciar Sesión"
    fun nextPage(){
        val intent = Intent(this, InicioSesion2::class.java).apply {  }
        startActivity(intent)
        /*Linea para cerrar activity y que no consuma recursos*/
        //finish()
    }
    //Función creadora de la activity dirigida al registro de usuario y asignada al botón "Registrarse"
    fun nextPageR(){
        val intent = Intent(this, PantallaRegistro::class.java).apply {  }
        startActivity(intent)
    }
    //Sobreescribir la funcion onClick para que por cada item diferente
    //Realice cierta actividad o función
    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.buttonIS ->
                nextPage()
            R.id.buttonR ->
                nextPageR()
        }
    }
    //Función que se ejecuta al iniciar la activity para verificar si existe una sesión abierta, que la cuenta este
    //verificada y así reedirigirlo a otra pantalla
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
    //Función creadora de la activity para mandar un correo de verificación
    private fun check(){
        val intent = Intent(this, CheckEmail::class.java).apply {  }
        startActivity(intent)
        finish()
    }
    //Función creadora de la activity dirigida al menu principal
    private fun reload(){
        val intent = Intent(this, PantallaPrincipal::class.java).apply {  }
        startActivity(intent)
        finish()
    }
}