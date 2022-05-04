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

    private lateinit var auth: FirebaseAuth
    private var btnVerificar: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_email)

        // Initialize Firebase Auth
        auth = Firebase.auth

        btnVerificar = findViewById<Button>(R.id.btnVerificar)

        btnVerificar!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnVerificar ->
                verificar()
        }
    }

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

    private fun reload(){
        val intent = Intent(this, PantallaPrincipal::class.java).apply {  }
        startActivity(intent)
        finish()
    }

    private fun verificar(){
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (user.isEmailVerified) {
                        val intent = Intent(this, PantallaPrincipal::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Por favor verifica tu correo.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private  fun signOut(){
        auth.signOut()
        val intent = Intent(this, InicioSesion::class.java)
        startActivity(intent)
    }
}