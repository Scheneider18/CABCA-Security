package com.example.cabca_app

import android.app.KeyguardManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricViewModel
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import java.util.concurrent.Executor

class PantallaEmergencia : AppCompatActivity() {

    private lateinit var newRecycler: RecyclerView
    private lateinit var newList: ArrayList<Alerta>
    private lateinit var imageId: Array<Int>
    private lateinit var typeAlert: Array<String>
    private var descrip: String = ""
    private var authen = false

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_emergencia)

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                Log.w("MY_APP_TAG", "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.w("MY_APP_TAG", "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.w("MY_APP_TAG", "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                // Prompts the user to create credentials that your app accepts.
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG)
                }
                startActivityForResult(enrollIntent, REQUEST_CODE)
            }
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        /*promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()*/
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación mediante biométricas.")
            .setSubtitle("Autenticate con alguna biométrica.")
            // Can't call setNegativeButtonText() and
            // setAllowedAuthenticators(... or DEVICE_CREDENTIAL) at the same time.
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()

        imageId = arrayOf(
            R.drawable.ic_baseline_security_24,
            R.drawable.ic_baseline_security_24_a,
            R.drawable.ic_baseline_security_24_r
        )

        typeAlert = arrayOf(
            "Alerta Baja",
            "Alerta Media",
            "Alerta Alta"
        )

        descrip = "Contacto registrado"

        newRecycler = findViewById(R.id.recyclerAlert)
        newRecycler.layoutManager = LinearLayoutManager(this)
        newRecycler.setHasFixedSize(true)

        newList = arrayListOf()

        getUserData()
    }

    private fun getUserData() {
        for (i in imageId.indices){
            val alert = Alerta(imageId[i],typeAlert[i],descrip)
            newList.add(alert)
        }

        val adapter = AdaptadorAlert(newList)
        newRecycler.adapter = adapter
        adapter.setOnItemClickListener(object : AdaptadorAlert.onItemClickListener{
            override fun onItemClick(position: Int) {
                biometricPrompt.authenticate(promptInfo)
                Toast.makeText(this@PantallaEmergencia, "Has clickeado el no. $position",Toast.LENGTH_SHORT).show()
            }

        })
    }

    //private var canAuthenticate = false
    //private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    /*private fun setupAuth(){
        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS){

            canAuthenticate = true

            promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Autentícate utilizando el sensor biométrico")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()
        }
    }

    private fun authenticate(){
        if (canAuthenticate){
            androidx.biometric.BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                object : androidx.biometric.BiometricPrompt.AuthenticationCallback(){
                    override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        authen = true
                        Toast.makeText(this@PantallaEmergencia, "Valor: $authen", Toast.LENGTH_SHORT).show()
                    }
                }).authenticate(promptInfo)
        }else{
            authen = true
            Toast.makeText(this@PantallaEmergencia, "Valor: $authen", Toast.LENGTH_SHORT).show()
        }
    }*/
}