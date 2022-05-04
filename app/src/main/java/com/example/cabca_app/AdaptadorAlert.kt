package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorAlert (private var alertList: ArrayList<Alerta>) : RecyclerView.Adapter<AdaptadorAlert.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.alert_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = alertList[position]
        holder.icon.setImageResource(currentItem.image)
        holder.type.text = currentItem.alerta
        holder.description.text = currentItem.descripcion
    }

    override fun getItemCount(): Int {
        return alertList.size
    }

}