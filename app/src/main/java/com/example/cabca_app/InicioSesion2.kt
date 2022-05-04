package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InicioSesion2 : AppCompatActivity(), View.OnClickListener {

    private var buttonLogin: Button? = null
    private var eTCorreo: EditText? = null
    private var eTPass: EditText? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion2)

        buttonLogin = findViewById(R.id.buttonLogin)
        eTCorreo = findViewById(R.id.eTCorreo)
        eTPass = findViewById(R.id.eTPass)

        // Initialize Firebase Auth
        auth = Firebase.auth

        buttonLogin!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buttonLogin ->{
                val email = eTCorreo?.text.toString()
                val password = eTPass?.text.toString()
                when {
                    email.isEmpty() || password.isEmpty() -> {
                        Toast.makeText(
                            baseContext, "Ingrese los datos solicitados.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        signIn(email, password)
                    }
                }
            }
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

    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Correo o contraseña incorrectos. Verifique datos dados.",
                        Toast.LENGTH_SHORT).show()
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