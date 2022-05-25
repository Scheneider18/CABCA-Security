package com.example.cabca_app

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebSettings
import android.webkit.WebViewClient

class VerCamara : AppCompatActivity() {
    //Crear las variables para enlazarlos elementos
    private var webViewCamara: WebView? = null
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_camara)
        //Inicializar los elementos
        webViewCamara = findViewById<WebView>(R.id.webViewCamara)
        //Configuración del elemento para su correcta funcionalidad
        webViewCamara!!.webChromeClient = object : WebChromeClient(){}
        webViewCamara!!.webViewClient = object : WebViewClient(){}
        val settings = webViewCamara!!.settings
        val newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0"
        settings.javaScriptEnabled = true
        settings.allowContentAccess = true
        webViewCamara!!.settings.userAgentString = newUA
        webViewCamara!!.setInitialScale(100)
        //Dirección URL cambiante dependiendo de la cámara
        webViewCamara!!.loadUrl("http://192.168.100.221")
    }
    //Sobreecribe la función onBackPressed para su correcta función
    override fun onBackPressed() {
        if(webViewCamara!!.canGoBack()){
            webViewCamara!!.goBack()
        }else{
            webViewCamara!!.destroy()
            finish()
        }
    }
}