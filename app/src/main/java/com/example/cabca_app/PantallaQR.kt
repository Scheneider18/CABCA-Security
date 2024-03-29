package com.example.cabca_app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream
import java.util.*

class PantallaQR : AppCompatActivity(), View.OnClickListener {
    //Crear las variables para enlazarlos elementos
    private lateinit var imViQR: ImageView
    private var buttonRe: Button? = null
    private var buttonSha: Button? = null
    private var btnScan: Button? = null
    private var data: String = " "
    private var direc = ""
    private var count: Int = 0
    private lateinit var bmp: Bitmap
    private var date = Date()
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_qr)
        //Inicializar Firebase Auth
        auth = Firebase.auth
        //Inicializar los elementos
        imViQR = findViewById(R.id.imViQR)
        buttonRe = findViewById(R.id.buttonReload)
        buttonSha = findViewById(R.id.buttonShare)
        btnScan = findViewById(R.id.btnScan)
        //Asignar el metodo OnClick a los botones
        buttonRe!!.setOnClickListener(this)
        buttonSha!!.setOnClickListener(this)
        btnScan!!.setOnClickListener(this)
        //Llamado a la función para precargar el código QR
        preLoad()
    }
    //Sobreescribir la funcion onClick para que por cada item diferente
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.buttonReload ->
                reLoad(data)
            R.id.btnScan ->
                lectorQR()
            R.id.buttonShare -> {
                shareCode(bmp)
            }
        }
    }
    //Función para crear la activity para leer el código QR
    private fun lectorQR(){
        val intent = Intent(this, LectorQR::class.java).apply {  }
        startActivity(intent)
    }
    //Función para mostrar información a partir de un evento
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

            super.onActivityResult(requestCode, resultCode, data)
        //}
    }
    //Función para crear y precargar un código QR
    private fun preLoad(){
        val user = auth.currentUser
        db.collection("contacts").document(user?.email.toString())
            .get()
            .addOnSuccessListener { contact ->
                Toast.makeText(this, "Se obtuvo dato.", Toast.LENGTH_SHORT).show()
                direc = contact.data?.get("name") as String
            }
        data = "Permitir acceso al visitante. Fecha de visita: ${date.toString()}. Visita del residente $direc"

        if (data.isEmpty()){
            Toast.makeText(this,"No se puede crear el código por falta de información.",Toast.LENGTH_SHORT).show()
        }else{
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE,512,512)
                val width = bitMatrix.width
                val heigth = bitMatrix.height
                bmp = Bitmap.createBitmap(width, heigth, Bitmap.Config.RGB_565)
                for (x in 0 until width){
                    for (y in 0 until heigth){
                        bmp.setPixel(x, y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                imViQR.setImageBitmap(bmp)
            }catch (e: WriterException){
                e.printStackTrace()
            }
        }
    }
    //Función para refrescar el código QR
    private fun reLoad(data: String){
        count++
        var new = data + count.toString()
        if (new.isEmpty()){
            Toast.makeText(this,"No se puede crear el código por falta de información.",Toast.LENGTH_SHORT).show()
        }else{
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(new, BarcodeFormat.QR_CODE,512,512)
                val width = bitMatrix.width
                val heigth = bitMatrix.height
                bmp = Bitmap.createBitmap(width, heigth, Bitmap.Config.RGB_565)
                for (x in 0 until width){
                    for (y in 0 until heigth){
                        bmp.setPixel(x, y, if (bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                    }
                }
                imViQR.setImageBitmap(bmp)
                Toast.makeText(this,new,Toast.LENGTH_SHORT).show()
            }catch (e: WriterException){
                e.printStackTrace()
            }
        }
    }
    //Función para compartir el código QR a través de alguna aplicación en formato de imagen
    private fun shareCode(bmp: Bitmap){
        var b = bmp
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver,b,"Title","Description")
        val imageUri = Uri.parse(path)
        share.putExtra(Intent.EXTRA_STREAM, imageUri)
        val chooser = Intent.createChooser(share, "Compartir con...")
        startActivity(chooser)
    }
}