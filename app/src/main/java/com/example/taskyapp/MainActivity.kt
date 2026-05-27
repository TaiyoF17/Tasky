package com.example.taskyapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.select_user_tasker)

        db = DatabaseHelper(this)
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val ivUserProfile = findViewById<ImageView>(R.id.ivUserProfile)
        val username = intent.getStringExtra("USERNAME") ?: "Usuario"
        tvUserName.text = username

        // Cargar foto de perfil desde BD (BLOB)
        val blob = db.getUserPhoto(username)
        if (blob != null) {
            val bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
            if (bitmap != null) {
                ivUserProfile.setImageBitmap(bitmap)
                ivUserProfile.imageTintList = null // Quitar el tinte azul del placeholder
            }
        }

        // Navegación al Perfil de Usuario (Nueva Pantalla)
        val userAction: () -> Unit = {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        tvUserName.setOnClickListener { userAction() }
        ivUserProfile.setOnClickListener { userAction() }

        val cardSolicitar = findViewById<CardView>(R.id.clRequestTaskCard)
        val cardRealizar = findViewById<CardView>(R.id.clPerformTaskCard)
        val cardMessages = findViewById<CardView>(R.id.clMessagesCard)
        val btnLogout = findViewById<ImageButton>(R.id.btnLogout)

        cardSolicitar.setOnClickListener {
            val intent = Intent(this, RequestTaskActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        cardRealizar.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        cardMessages.setOnClickListener {
            val intent = Intent(this, ChatListActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // Borrar sesión
            val sharedPrefs = getSharedPreferences("TaskyPrefs", MODE_PRIVATE)
            sharedPrefs.edit().remove("ACTIVE_USER").apply()
            
            Toast.makeText(this, getString(R.string.logout_confirm), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refrescar la foto por si se cambió en el perfil
        val tvUserName = findViewById<TextView>(R.id.tvUserName)
        val ivUserProfile = findViewById<ImageView>(R.id.ivUserProfile)
        val username = tvUserName.text.toString()
        
        if (username != "Usuario") {
            val blob = db.getUserPhoto(username)
            if (blob != null) {
                val bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
                if (bitmap != null) {
                    ivUserProfile.setImageBitmap(bitmap)
                    ivUserProfile.imageTintList = null // Quitar el tinte al refrescar
                }
            }
        }
    }
}
