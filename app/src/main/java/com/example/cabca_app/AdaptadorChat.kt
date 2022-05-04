package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorChat(private val contactList: MutableList<Chat>) :
    RecyclerView.Adapter<AdaptadorChat.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder (itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val imgCont: ImageView = itemView.findViewById(R.id.imChatAvatar)
        val txViDirec: TextView = itemView.findViewById(R.id.txChatDireccion)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contactList[position]
        holder.imgCont.setImageResource(currentItem.image)
        holder.txViDirec.text = currentItem.name
        //holder.txViMes.text = currentItem.users as String
    }

    override fun getItemCount(): Int {
        return  contactList.size
    }
}