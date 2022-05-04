package com.example.cabca_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class AdaptadorMessage (private val messageList: MutableList<Message>, private val user: String) :
    RecyclerView.Adapter<AdaptadorMessage.ViewHolder>() {

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val myMessage: TextView = itemView.findViewById(R.id.myMessageTextView)
        val othersMessage: TextView = itemView.findViewById(R.id.othersMessageTextView)
        val myLayout: ConstraintLayout = itemView.findViewById(R.id.myMessageLayout)
        val otherLayout: ConstraintLayout = itemView.findViewById(R.id.otherMessageLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]

        if(user == message.from){
            holder.myLayout.visibility = View.VISIBLE
            holder.otherLayout.visibility = View.GONE

            holder.myMessage.text = message.message
        } else {
            holder.myLayout.visibility = View.GONE
            holder.otherLayout.visibility = View.VISIBLE

            holder.othersMessage.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

}