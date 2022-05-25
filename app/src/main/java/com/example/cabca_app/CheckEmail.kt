package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class CheckEmail : AppCompatActivity(), View.OnClickListener {
    //Crear las variables para enlazarlos elementos
    private lateinit var auth: FirebaseAuth
    private var btnVerificar: Button? = null
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_email)
        //Inicializar Firebase Auth
        auth = Firebase.auth
        //Inicializar los elementos
        btnVerificar = findViewById<Button>(R.id.btnVerificar)
        //Asignar el metodo OnClick a los botones
        btnVerificar!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnVerificar ->
                verificar()
        }
    }
    //Función para verificar si existe una sesión iniciada y si esta verificado su correo y pueda acceder
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                reload()
            } else {
                sendEmailVerification()
            }
        }
    }
    //Función para enviar un correo de verificación al correo establecido por el usuario
    private fun sendEmailVerification() {
        val user = auth.currentUser
        user!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Se ha enviado un correo de verifiación.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    //Función para iniciar la activity de la pantalla principal y cerrar esta
    private fun reload(){
        val intent = Intent(this, PantallaPrincipal::class.java).apply {  }
        startActivity(intent)
        finish()
    }
    //Función para solicitar acceso al sistema despues de verificar el correo
    private fun verificar(){
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (user.isEmailVerified) {
                        reload()
                    } else {
                        Toast.makeText(this, "Por favor verifica tu correo.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}