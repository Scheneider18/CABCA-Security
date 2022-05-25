package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//Clase creada para poder enlistar las alertas sin necesidad de demasiado código
class AdaptadorAlert (private var alertList: ArrayList<Alerta>) : RecyclerView.Adapter<AdaptadorAlert.ViewHolder>(){
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
    class ViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.iconAlert)
        val type: TextView = itemView.findViewById(R.id.typeAlert)
        val description: TextView = itemView.findViewById(R.id.description)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    //Función que carga la vista dentro de un adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
        return ViewHolder(itemView, mListener)
    }
    //Función que asigna la información que mostrará cada elemento del adapter
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = alertList[position]
        holder.icon.setImageResource(currentItem.image)
        holder.type.text = currentItem.alerta
        holder.description.text = currentItem.descripcion
    }
    //Función que retorna el tamaño de la lista
    override fun getItemCount(): Int {
        return alertList.size
    }
}