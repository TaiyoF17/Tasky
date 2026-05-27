package com.example.taskyapp

import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class RegistroActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        db = DatabaseHelper(this)

        val etUsuario = findViewById<EditText>(R.id.etNombreRegistro)
        val etCorreo = findViewById<EditText>(R.id.etEmailRegistro)
        val etPass = findViewById<EditText>(R.id.etPasswordRegistro)
        val btnBack = findViewById<ImageButton>(R.id.btnBackRegistro)
        val btnRegistrarse = findViewById<MaterialButton>(R.id.btnRegistrarse)

        btnBack.setOnClickListener {
            finish()
        }

        btnRegistrarse.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val pass = etPass.text.toString().trim()

            if (usuario.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!correo.contains("@")) {
                Toast.makeText(this, "El correo debe contener un '@'", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                Toast.makeText(this, getString(R.string.error_short_password), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = db.registerUser(usuario, pass)
            if (id != -1L) {
                Toast.makeText(this, getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.registration_error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}