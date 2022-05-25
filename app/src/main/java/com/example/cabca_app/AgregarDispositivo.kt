package com.example.cabca_app

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AgregarDispositivo : AppCompatActivity(), View.OnClickListener {
    //Crear las variables para enlazarlos elementos
    lateinit var badapter: BluetoothAdapter
    private val REQUEST_CODE_ENABLE_BT: Int = 1;
    private lateinit var newRecycler: RecyclerView
    private lateinit var newList: ArrayList<Device>
    private var btnSearch: Button? = null
    //Función creadora de la activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_dispositivo)
        //Inicializar los elementos
        badapter = BluetoothAdapter.getDefaultAdapter()
        newRecycler = findViewById(R.id.recyclerDevice)
        newRecycler.layoutManager = LinearLayoutManager(this)
        newRecycler.setHasFixedSize(true)
        newList = arrayListOf<Device>()
        btnSearch = findViewById(R.id.btnSearchDevice)
        //Asignar el metodo OnClick a los botones
        btnSearch!!.setOnClickListener(this)
        //Llamado a la función que solicita los permisos de uso del bluetooth al usuario
        permisoBluetooth()
    }
    //Funcion para enlistar los dispositivos disponibles para conectar mediante bluetooth
    private fun listDevice(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (badapter.isEnabled){
                val devices = badapter.bondedDevices
                for (device in devices){
                    val nombre = device.name
                    val direccion = device.address
                    val dispositivo = Device(nombre,direccion)
                    newList.add(dispositivo)
                    Log.w("TAG", "Dispositivo ${dispositivo}")
                }
                var adapter = AdaptadorDevice(newList)
                newRecycler.adapter = adapter
                adapter.setOnItemClickListener(object : AdaptadorDevice.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        val intent = Intent(this@AgregarDispositivo, PantallaCamara::class.java)
                        startActivity(intent)
                        Toast.makeText(this@AgregarDispositivo, "Has seleccionado un dispositivo",Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }
    //Función para solicitar permisos al usuario para utilizar el servicio de bluetooth
    private fun permisoBluetooth(){
        if (badapter.isEnabled()){
            Toast.makeText(this, "Bluetooth esta encendido",Toast.LENGTH_SHORT).show()
        }else{

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
            }

        }
    }
    //Función para verificar el estado del permiso de uso de bluetooth
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "Bluetooth esta encendido",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Bluetooth no se puede encender",Toast.LENGTH_SHORT).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //Sobreescribir la funcion onClick para que por cada item diferente
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btnSearchDevice ->
                listDevice()
        }
    }
}