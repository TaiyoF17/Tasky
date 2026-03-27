package com.example.taskyfinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class RealizarTareaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realizar_tarea)

        val cardTareaExample = findViewById<View>(R.id.cardTareaExample)
        
        cardTareaExample.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }
    }
}