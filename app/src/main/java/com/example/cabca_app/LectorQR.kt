package com.example.cabca_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import javax.annotation.meta.When

class LectorQR : AppCompatActivity(), View.OnClickListener {

    private var btnScan: Button? = null
    private var txtDatos: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lector_qr)

        btnScan = findViewById(R.id.btnReadQR)
        txtDatos = findViewById(R.id.txtViewDatos)

        btnScan!!.setOnClickListener(this)
    }

    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Coloca el código en el recuadro.")
        integrator.setTorchEnabled(true)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null){
            if(result.contents == null){
                Toast.makeText(this,"Cancelado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"${result.contents}", Toast.LENGTH_SHORT).show()
                txtDatos?.text = result.contents
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnReadQR ->
                initScanner()
        }
    }
}