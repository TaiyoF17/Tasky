package com.example.taskyapp

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RequestTaskActivity : AppCompatActivity() {
    
    private lateinit var db: DatabaseHelper
    private var currentUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)
        currentUser = intent.getStringExtra("USERNAME") ?: ""

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val tvUserLabel = findViewById<TextView>(R.id.tvUserLabel)
        val ivUserIcon = findViewById<ImageView>(R.id.ivUserIcon)
        
        val etTarea = findViewById<EditText>(R.id.etTarea)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etDireccion = findViewById<EditText>(R.id.etDireccion)
        val etPago = findViewById<EditText>(R.id.etPago)
        val btnSolicitar = findViewById<Button>(R.id.button_solicitar)

        // Cargar nombre y foto del usuario actual
        tvUserLabel.text = if (currentUser.isNotEmpty()) currentUser else "Usuario"
        
        val blob = db.getUserPhoto(currentUser)
        if (blob != null) {
            val bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
            if (bitmap != null) {
                ivUserIcon.setImageBitmap(bitmap)
                ivUserIcon.imageTintList = null // Quitar el tinte azul del placeholder
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnSolicitar.setOnClickListener {
            val tarea = etTarea.text.toString()
            val descripcion = etDescripcion.text.toString()
            val direccion = etDireccion.text.toString()
            val pagoStr = etPago.text.toString()

            if (tarea.isNotEmpty() && descripcion.isNotEmpty() && direccion.isNotEmpty() && pagoStr.isNotEmpty()) {
                val pago = pagoStr.toDoubleOrNull() ?: 0.0
                val id = db.insertTask(tarea, descripcion, direccion, pago, currentUser)
                if (id != -1L) {
                    Toast.makeText(this, "Tarea solicitada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}