package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//Clase creada para poder enlistar los dispositivos bluetooth sin necesidad de demasiado código
class AdaptadorDevice (private var deviceList: ArrayList<Device>) : RecyclerView.Adapter<AdaptadorDevice.ViewHolder>() {
    //Variable del onItemClickListener
    private lateinit var mListener: onItemClickListener
    //Interfaz del onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    //Función que crea un evento de onItemClickListener para cada elemento
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }
    //Clase que enlaza la vista con la clase rellenadora
    class ViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.textViewDeviceName)
        val direccion: TextView = itemView.findViewById(R.id.textViewDeviceAddress)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    //Función que asigna la información que mostrará cada elemento del adapter
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = deviceList[position]
        holder.nombre.text = currentItem.name
        holder.direccion.text = currentItem.address
    }
    //Función que carga la vista dentro de un adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return ViewHolder(itemView, mListener)
    }
    //Función que retorna el tamaño de la lista
    override fun getItemCount(): Int {
        return deviceList.size
    }

}