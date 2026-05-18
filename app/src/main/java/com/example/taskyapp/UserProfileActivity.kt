package com.example.taskyapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.ByteArrayOutputStream

class UserProfileActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var ivProfile: ImageView
    private lateinit var adapter: TaskAdapter
    private var currentUser: String = ""

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                try {
                    val bitmap = uriToBitmap(imageUri)
                    if (bitmap != null) {
                        ivProfile.setImageBitmap(bitmap)
                        ivProfile.imageTintList = null // Quitar el tinte gris del placeholder
                        val blob = bitmapToByteArray(bitmap)
                        db.updateUserPhoto(currentUser, blob)
                        Toast.makeText(this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("UserProfile", "Error processing image", e)
                    Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_user_profile)

            db = DatabaseHelper(this)
            currentUser = intent.getStringExtra("USERNAME") ?: ""

            val btnBack = findViewById<ImageButton>(R.id.btnBackProfile)
            val tvUserName = findViewById<TextView>(R.id.tvProfileUserName)
            val fabChangePhoto = findViewById<FloatingActionButton>(R.id.fabChangePhoto)
            ivProfile = findViewById(R.id.ivUserProfileLarge)

            tvUserName.text = currentUser
            
            // Cargar imagen guardada desde la base de datos (BLOB)
            loadProfileImageFromDB()

            btnBack.setOnClickListener { finish() }

            fabChangePhoto.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                selectImageLauncher.launch(intent)
            }

            setupHistoryRecyclerView()
        } catch (e: Exception) {
            Log.e("UserProfile", "Crash in onCreate", e)
            Toast.makeText(this, "Error crítico al abrir el perfil", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun loadProfileImageFromDB() {
        val blob = db.getUserPhoto(currentUser)
        if (blob != null) {
            val bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.size)
            if (bitmap != null) {
                ivProfile.setImageBitmap(bitmap)
                ivProfile.imageTintList = null // Quitar el tinte gris al cargar desde BD
            }
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        // Comprimimos la imagen para que no ocupe demasiado en la BD (formato JPEG, calidad 70)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    private fun setupHistoryRecyclerView() {
        val rvHistory = findViewById<RecyclerView>(R.id.rvUserTaskHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)

        try {
            val userTasks = db.getUserTasks(currentUser)
            adapter = TaskAdapter(userTasks, showCancelButton = true) { task ->
                cancelTask(task)
            }
            rvHistory.adapter = adapter
        } catch (e: Exception) {
            Log.e("UserProfile", "Error loading tasks", e)
            Toast.makeText(this, "Error al cargar el historial", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelTask(task: TaskAdapter.Task) {
        val deletedRows = db.deleteTask(task.id)
        if (deletedRows > 0) {
            adapter.removeTask(task)
            Toast.makeText(this, "Tarea cancelada", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al cancelar la tarea", Toast.LENGTH_SHORT).show()
        }
    }
}