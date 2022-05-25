package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//Clase creada para poder enlistar los chats disponibles del usuario sin necesidad de demasiado código
class AdaptadorChat(private val contactList: MutableList<Chat>) :
    RecyclerView.Adapter<AdaptadorChat.ViewHolder>() {
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
        val imgCont: ImageView = itemView.findViewById(R.id.imChatAvatar)
        val txViDirec: TextView = itemView.findViewById(R.id.txChatDireccion)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
    //Función que carga la vista dentro de un adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ViewHolder(itemView, mListener)
    }
    //Función que asigna la información que mostrará cada elemento del adapter
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contactList[position]
        holder.imgCont.setImageResource(currentItem.image)
        holder.txViDirec.text = currentItem.name
        //holder.txViMes.text = currentItem.users as String
    }
    //Función que retorna el tamaño de la lista
    override fun getItemCount(): Int {
        return  contactList.size
    }
}