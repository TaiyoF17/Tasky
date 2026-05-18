package com.example.taskyapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatListActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var currentUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        db = DatabaseHelper(this)
        currentUser = intent.getStringExtra("USERNAME") ?: ""

        val toolbar = findViewById<Toolbar>(R.id.toolbarChatList)
        setSupportActionBar(toolbar)
        
        // Configurar icono de navegación y pintarlo de blanco
        val navIcon = androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_back)
        navIcon?.let {
            val wrappedDrawable = androidx.core.graphics.drawable.DrawableCompat.wrap(it)
            androidx.core.graphics.drawable.DrawableCompat.setTint(wrappedDrawable, android.graphics.Color.WHITE)
            toolbar.navigationIcon = wrappedDrawable
        }

        toolbar.setNavigationOnClickListener { finish() }

        val rvChatPartners = findViewById<RecyclerView>(R.id.rvChatPartners)
        rvChatPartners.layoutManager = LinearLayoutManager(this)

        val partners = db.getUserChatPartners(currentUser)
        rvChatPartners.adapter = ChatPartnerAdapter(partners) { partner ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("CURRENT_USER", currentUser)
            intent.putExtra("OTHER_USER", partner)
            startActivity(intent)
        }
    }

    inner class ChatPartnerAdapter(
        private val partners: List<String>,
        private val onClick: (String) -> Unit
    ) : RecyclerView.Adapter<ChatPartnerAdapter.PartnerViewHolder>() {

        inner class PartnerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvPartnerName)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_partner, parent, false)
            return PartnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: PartnerViewHolder, position: Int) {
            val partner = partners[position]
            holder.tvName.text = partner
            holder.itemView.setOnClickListener { onClick(partner) }
        }

        override fun getItemCount() = partners.size
    }
}