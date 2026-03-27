package com.example.taskyfinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnComenzar = findViewById<Button>(R.id.btnComenzar)

        btnComenzar.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}