package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorContacto(private var contactsList: MutableList<Contactos>) : RecyclerView.Adapter<AdaptadorContacto.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val imgCont: ImageView = itemView.findViewById(R.id.imContAvatar)
        val txViDirec: TextView = itemView.findViewById(R.id.txContDireccion)
        val txViMes: TextView = itemView.findViewById(R.id.txContCorreo)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contactsList[position]
        holder.imgCont.setImageResource(currentItem.image)
        holder.txViDirec.text = currentItem.name
        holder.txViMes.text = currentItem.correo
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }
}