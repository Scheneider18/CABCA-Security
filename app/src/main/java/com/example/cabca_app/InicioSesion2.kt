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
    //Variables para el uso de elementos visuales y lógicos
    private var buttonLogin: Button? = null
    private var eTCorreo: EditText? = null
    private var eTPass: EditText? = null
    private lateinit var auth: FirebaseAuth
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion2)
        //Inicializar los elementos
        buttonLogin = findViewById(R.id.buttonLogin)
        eTCorreo = findViewById(R.id.eTCorreo)
        eTPass = findViewById(R.id.eTPass)
        //Inicializar Firebase Auth
        auth = Firebase.auth
        //Asignar el metodo OnClick al botón
        buttonLogin!!.setOnClickListener(this)
    }
    //Sobreescribir la funcion onClick para que por cada item diferente
    //Realice cierta actividad o función
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
    //Función de inicio de sesión, en la cual es enviado el correo y contraseña a Firebase y verificar que exista y verificar que este verificado
    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    check()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Correo o contraseña incorrectos. Verifique datos dados.",
                        Toast.LENGTH_SHORT).show()
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