package com.example.taskyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Aquí le decimos a la Activity que muestre tu diseño del menú
        setContentView(R.layout.select_user_tasker)

        // 2. Buscamos la tarjeta "Solicitar tarea" por su ID
        val cardSolicitar = findViewById<ConstraintLayout>(R.id.clRequestTaskCard)

        // 3. Le decimos qué hacer cuando el usuario la toque
        cardSolicitar.setOnClickListener {

            // 4. Creamos la "intención" de viajar a la nueva pantalla que creaste
            val intent = Intent(this, RequestTaskActivity::class.java)

            // 5. ¡Iniciamos el viaje!
            startActivity(intent)
        }
    }
}