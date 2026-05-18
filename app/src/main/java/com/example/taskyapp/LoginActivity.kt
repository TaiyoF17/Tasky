package com.example.taskyapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        db = DatabaseHelper(this)
        
        // Verificar si hay una sesión activa
        val sharedPrefs = getSharedPreferences("TaskyPrefs", MODE_PRIVATE)
        val activeUser = sharedPrefs.getString("ACTIVE_USER", null)
        
        if (activeUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USERNAME", activeUser)
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etUser = findViewById<EditText>(R.id.etUser)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoToRegistro = findViewById<TextView>(R.id.tvGoToRegistro)

        btnLogin.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPassword.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                if (db.checkUser(user, pass)) {
                    // Guardar sesión
                    val sharedPrefs = getSharedPreferences("TaskyPrefs", MODE_PRIVATE)
                    sharedPrefs.edit().putString("ACTIVE_USER", user).apply()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USERNAME", user)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        tvGoToRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}