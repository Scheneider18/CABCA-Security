package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class PantallaCamara : AppCompatActivity(), View.OnClickListener {

    private var buttonAdd: Button? = null
    private var imBtnCamara1: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_camara)

        buttonAdd = findViewById<Button>(R.id.buttonAdd)
        imBtnCamara1 = findViewById<ImageButton>(R.id.imBtnCamera1)

        buttonAdd!!.setOnClickListener(this)
        imBtnCamara1!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buttonAdd ->
                addDevice()
            R.id.imBtnCamera1 ->
                viewCamara()
        }
    }

    private fun viewCamara(){
        val intent = Intent(this, VerCamara::class.java).apply {  }
        startActivity(intent)
    }

    private fun addDevice(){
        val intent = Intent(this, AgregarDispositivo::class.java).apply {  }
        startActivity(intent)
    }
}