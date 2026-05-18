package com.example.taskyapp

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(
    private val messages: MutableList<DatabaseHelper.Message>,
    private val currentUser: String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText: TextView = view.findViewById(R.id.tvMessageText)
        val tvTime: TextView = view.findViewById(R.id.tvMessageTimestamp)
        val llBubble: LinearLayout = view.findViewById(R.id.llMessageBubble)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.tvText.text = message.text
        holder.tvTime.text = message.timestamp

        val params = holder.llBubble.layoutParams as LinearLayout.LayoutParams
        if (message.sender == currentUser) {
            params.gravity = Gravity.END
            holder.llBubble.setBackgroundResource(R.drawable.bg_message_sent)
        } else {
            params.gravity = Gravity.START
            holder.llBubble.setBackgroundResource(R.drawable.bg_message_received)
        }
        holder.llBubble.layoutParams = params
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: DatabaseHelper.Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}