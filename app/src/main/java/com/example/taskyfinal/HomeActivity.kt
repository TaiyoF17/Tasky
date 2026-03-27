package com.example.taskyfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val cardSolicitar = findViewById<View>(R.id.cardSolicitar)
        val cardRealizar = findViewById<View>(R.id.cardRealizar)
        
        cardSolicitar.setOnClickListener {
            val intent = Intent(this, SolicitarTareaActivity::class.java)
            startActivity(intent)
        }

        cardRealizar.setOnClickListener {
            val intent = Intent(this, RealizarTareaActivity::class.java)
            startActivity(intent)
        }
    }
}