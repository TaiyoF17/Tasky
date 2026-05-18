package com.example.taskyapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskListActivity : AppCompatActivity() {
    
    private lateinit var db: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private var currentUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        db = DatabaseHelper(this)
        
        // Obtener usuario actual desde SharedPreferences o intent
        val sharedPrefs = getSharedPreferences("TaskyPrefs", MODE_PRIVATE)
        currentUser = sharedPrefs.getString("ACTIVE_USER", null)

        findViewById<ImageButton>(R.id.btnBackList).setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.rvTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        val tasks = db.getAllTasks()
        adapter = TaskAdapter(
            tasks,
            onCompleteClick = { task ->
                db.deleteTask(task.id)
                adapter.removeTask(task)
            },
            onItemClick = { task ->
                if (currentUser != null && task.requester != null && currentUser != task.requester) {
                    val intent = android.content.Intent(this, ChatActivity::class.java)
                    intent.putExtra("CURRENT_USER", currentUser)
                    intent.putExtra("OTHER_USER", task.requester)
                    startActivity(intent)
                }
            }
        )
        recyclerView.adapter = adapter
    }
}