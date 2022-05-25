package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class PantallaCamara : AppCompatActivity(), View.OnClickListener {
    //Crear las variables para enlazarlos elementos
    private var buttonAdd: Button? = null
    private var imBtnCamara1: ImageButton? = null
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_camara)
        //Inicializar los elementos
        buttonAdd = findViewById<Button>(R.id.buttonAdd)
        imBtnCamara1 = findViewById<ImageButton>(R.id.imBtnCamera1)
        //Asignar el metodo OnClick a los botones
        buttonAdd!!.setOnClickListener(this)
        imBtnCamara1!!.setOnClickListener(this)
    }
    //Sobreescribir la funcion onClick para que por cada item diferente
    //Realice la función de ver o agregar cámara
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buttonAdd ->
                addDevice()
            R.id.imBtnCamera1 ->
                viewCamara()
        }
    }
    //Función para iniciar la activity de la visualización de la cámara
    private fun viewCamara(){
        val intent = Intent(this, VerCamara::class.java).apply {  }
        startActivity(intent)
    }
    //Función para iniciar la activity para añadir una nueva cámara
    private fun addDevice(){
        val intent = Intent(this, AgregarDispositivo::class.java).apply {  }
        startActivity(intent)
    }
}