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
    //Crear las variables para enlazarlos elementos
    private lateinit var newRecycler: RecyclerView
    private lateinit var newList: ArrayList<Alerta>
    private lateinit var imageId: Array<Int>
    private lateinit var typeAlert: Array<String>
    private var descrip: String = ""
    private var authen = false
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_emergencia)
        //Configuración para la validación a través del uso de patron o por alguna función biométrica obtenida del celular
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
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación mediante biométricas.")
            .setSubtitle("Autenticate con alguna biométrica.")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
        //Inicializar los elementos
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
        //Llamado a la función para poder cargar los datos de las alertas
        getUserData()
    }
    //Función para enlistar las alertas en un adapter
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
}