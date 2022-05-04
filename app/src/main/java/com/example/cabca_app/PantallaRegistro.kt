package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class PantallaRegistro : AppCompatActivity(), View.OnClickListener {

    private var buttonRe: Button? = null
    private var eTNom: EditText? = null
    private var eTDirec: EditText? = null
    private var eTCalle: EditText? = null
    private var eTNum: EditText? = null
    private var eTEmail: EditText? = null
    private var eTCel: EditText? = null
    private var eTCont: EditText? = null
    private var eTContConf: EditText? = null
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var imageId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_registro)

        buttonRe = findViewById<Button>(R.id.buttonRe)
        eTNom = findViewById<EditText>(R.id.eTNom)
        eTDirec = findViewById<EditText>(R.id.eTDirec)
        eTCalle = findViewById<EditText>(R.id.eTCalle)
        eTNum = findViewById<EditText>(R.id.eTNum)
        eTEmail = findViewById<EditText>(R.id.eTEmail)
        eTCel = findViewById<EditText>(R.id.eTCel)
        eTCont = findViewById<EditText>(R.id.eTCont)
        eTContConf = findViewById<EditText>(R.id.eTContConf)

        imageId = R.drawable.logogrande

        // Initialize Firebase Auth
        auth = Firebase.auth

        buttonRe!!.setOnClickListener(this)
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified){
                val intent = Intent(this, PantallaPrincipal::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, CheckEmail::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buttonRe -> {
                val email = eTEmail?.text.toString()
                val password = eTCont?.text.toString()
                val conf = eTContConf?.text.toString()
                val name = eTNom?.text.toString()
                val address = eTDirec?.text.toString()
                val calle = eTCalle?.text.toString()
                val numero = eTNum?.text.toString()
                val cel = eTCel?.text.toString()
                val passwordRegex = Pattern.compile("^" +
                        "(?=.*[-@#$%^&+=])" +     // Al menos 1 carácter especial
                        ".{6,}" +                // Al menos 4 caracteres
                        "$")
                if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Ingrese un email valido.",
                        Toast.LENGTH_SHORT).show()
                } else if (password.isEmpty() || !passwordRegex.matcher(password).matches()){
                    Toast.makeText(this, "La contraseña es debil.",
                        Toast.LENGTH_SHORT).show()
                } else if (password != conf){
                    Toast.makeText(this, "Confirma la contraseña.",
                        Toast.LENGTH_SHORT).show()
                } else if (name.isEmpty() || address.isEmpty() || calle.isEmpty() || numero.isEmpty() || cel.isEmpty()){
                    Toast.makeText(this, "Rellene todos los campos",
                        Toast.LENGTH_SHORT).show()
                }else{
                    signUp(email, password)
                }
            }

        }
    }

    private fun signUp(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    save()

                    val intent = Intent(this, CheckEmail::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun save(){
        val user = auth.currentUser
        if(user != null){
            db.collection("users").document(user.email as String).set(
                hashMapOf("nombre" to eTNom?.text.toString(),
                "direccion" to eTDirec?.text.toString(),
                "calle" to eTCalle?.text.toString(),
                "numero_calle" to eTNum?.text.toString(),
                "numero_celular" to eTCel?.text.toString(),
                "correo" to user.email as String)
            )
        }
        save2()
    }

    private fun save2(){
        val user = auth.currentUser
        if (user != null){
            db.collection("contacts").document(user.email as String).set(
                hashMapOf("image" to imageId,
                    "name" to "${eTCalle?.text.toString()} ${eTNum?.text.toString()}",
                    "correo" to user.email as String)
            )
        }
    }
}