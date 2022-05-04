package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorDevice (private var deviceList: ArrayList<Device>) : RecyclerView.Adapter<AdaptadorDevice.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val nombre: TextView = itemView.findViewById(R.id.textViewDeviceName)
        val direccion: TextView = itemView.findViewById(R.id.textViewDeviceAddress)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = deviceList[position]
        holder.nombre.text = currentItem.name
        holder.direccion.text = currentItem.address
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

}