package com.example.taskyapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var adapter: MessageAdapter
    private lateinit var currentUser: String
    private lateinit var otherUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        db = DatabaseHelper(this)
        currentUser = intent.getStringExtra("CURRENT_USER") ?: ""
        otherUser = intent.getStringExtra("OTHER_USER") ?: ""

        val toolbar = findViewById<Toolbar>(R.id.toolbarChat)
        toolbar.title = "Chat con $otherUser"
        setSupportActionBar(toolbar)
        
        // Configurar icono de navegación y pintarlo de azul
        val navIcon = androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_back)
        navIcon?.let {
            val wrappedDrawable = androidx.core.graphics.drawable.DrawableCompat.wrap(it)
            androidx.core.graphics.drawable.DrawableCompat.setTint(wrappedDrawable, androidx.core.content.ContextCompat.getColor(this, R.color.tasky_blue))
            toolbar.navigationIcon = wrappedDrawable
        }

        toolbar.setNavigationOnClickListener { finish() }

        val rvMessages = findViewById<RecyclerView>(R.id.rvMessages)
        rvMessages.layoutManager = LinearLayoutManager(this)
        
        val messages = db.getMessages(currentUser, otherUser)
        adapter = MessageAdapter(messages, currentUser)
        rvMessages.adapter = adapter
        rvMessages.scrollToPosition(messages.size - 1)

        val etMessage = findViewById<EditText>(R.id.etMessage)
        val btnSend = findViewById<ImageButton>(R.id.btnSendMessage)

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                val id = db.insertMessage(currentUser, otherUser, text)
                if (id > -1) {
                    // Refrescar para obtener el timestamp real o usar uno local temporal
                    val newMessage = DatabaseHelper.Message(currentUser, otherUser, text, "Ahora")
                    adapter.addMessage(newMessage)
                    rvMessages.scrollToPosition(adapter.itemCount - 1)
                    etMessage.text.clear()
                }
            }
        }
    }
}