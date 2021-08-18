package com.example.firestorechatapp

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val chatData: List<Message>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val SENT = 0
    private val RECEIVED = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){

            SENT -> {
                val tempHolder: SentHolder = holder as SentHolder
                tempHolder.sentTV.text = chatData[position].message

                val sentByString = "Sent by " + chatData[position].name + " at " + chatData[position].time
                tempHolder.timeStamp.text = sentByString

            }
            RECEIVED -> {
                val tempHolder: ReceivedHolder = holder as ReceivedHolder
                holder.receivedTV.text = chatData[position].message

                val sentByString = "Sent by " + chatData[position].name + " at " + chatData[position].time
                tempHolder.timeStamp.text = sentByString
            }

        }
    }

    override fun getItemViewType(position: Int): Int {

        when(chatData[position].type){
            Constants.MESSAGE_TYPE_SENT -> return SENT
            Constants.MESSAGE_TYPE_RECEIVED -> return RECEIVED
        }
        return -1
    }

    override fun getItemCount(): Int {
        return chatData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            SENT -> {
                val view = LayoutInflater.from(context).inflate(R.layout.sent_layout,parent,false)
                SentHolder(view)
            }
            RECEIVED -> {
                val view = LayoutInflater.from(context).inflate(R.layout.received_layout,parent,false)
                ReceivedHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.sent_layout,parent,false)
                SentHolder(view)
            }
        }
    }

    inner class SentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var sentTV: TextView = itemView.findViewById(R.id.sentMessage)
        var timeStamp: TextView = itemView.findViewById(R.id.timeStamp)
    }

    inner class ReceivedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var receivedTV: TextView = itemView.findViewById(R.id.receivedMessage)
        var timeStamp: TextView = itemView.findViewById(R.id.timeStamp)
    }

}