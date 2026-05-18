package com.example.taskyapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val TAG = "DatabaseHelper"
        private const val DATABASE_NAME = "tasky_v6.db"
        private const val DATABASE_VERSION = 1

        // Tabla Usuarios
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "username"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_PHOTO = "photo"

        // Tabla Tareas
        const val TABLE_TASKS = "tasks"
        const val COLUMN_TASK_ID = "id"
        const val COLUMN_TASK_TITLE = "title"
        const val COLUMN_TASK_DESC = "description"
        const val COLUMN_TASK_ADDRESS = "address"
        const val COLUMN_TASK_PAYMENT = "payment"
        const val COLUMN_TASK_USER = "created_by"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating tables...")
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER_NAME TEXT UNIQUE, " +
                "$COLUMN_USER_PASSWORD TEXT, " +
                "$COLUMN_USER_PHOTO BLOB)")
        db.execSQL(createUsersTable)

        val createTasksTable = ("CREATE TABLE $TABLE_TASKS (" +
                "$COLUMN_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TASK_TITLE TEXT, " +
                "$COLUMN_TASK_DESC TEXT, " +
                "$COLUMN_TASK_ADDRESS TEXT, " +
                "$COLUMN_TASK_PAYMENT REAL, " +
                "$COLUMN_TASK_USER TEXT, " +
                "FOREIGN KEY($COLUMN_TASK_USER) REFERENCES $TABLE_USERS($COLUMN_USER_NAME))")
        db.execSQL(createTasksTable)
        Log.d(TAG, "Tables created successfully with Foreign Key.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    // Funciones para Usuarios
    fun registerUser(user: String, pass: String): Long {
        Log.d(TAG, "Registering user: $user")
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user)
        values.put(COLUMN_USER_PASSWORD, pass)
        val id = db.insert(TABLE_USERS, null, values)
        Log.d(TAG, "User registered with ID: $id")
        return id
    }

    fun checkUser(user: String, pass: String): Boolean {
        Log.d(TAG, "Checking user: $user")
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_NAME=? AND $COLUMN_USER_PASSWORD=?", arrayOf(user, pass))
        val count = cursor.count
        Log.d(TAG, "Found $count matches for user $user")
        val exists = count > 0
        cursor.close()
        return exists
    }

    fun updateUserPhoto(username: String, photo: ByteArray): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_PHOTO, photo)
        return db.update(TABLE_USERS, values, "$COLUMN_USER_NAME=?", arrayOf(username))
    }

    fun getUserPhoto(username: String): ByteArray? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_PHOTO), "$COLUMN_USER_NAME=?", arrayOf(username), null, null, null)
        var photo: ByteArray? = null
        if (cursor.moveToFirst()) {
            photo = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_USER_PHOTO))
        }
        cursor.close()
        return photo
    }

    // Funciones para Tareas
    fun insertTask(title: String, desc: String, address: String, payment: Double, username: String): Long {
        Log.d(TAG, "Inserting task for user $username: $title")
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TASK_TITLE, title)
        values.put(COLUMN_TASK_DESC, desc)
        values.put(COLUMN_TASK_ADDRESS, address)
        values.put(COLUMN_TASK_PAYMENT, payment)
        values.put(COLUMN_TASK_USER, username)
        val id = db.insert(TABLE_TASKS, null, values)
        Log.d(TAG, "Task inserted with ID: $id")
        return id
    }

    fun getAllTasks(): MutableList<TaskAdapter.Task> {
        val tasks = mutableListOf<TaskAdapter.Task>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESC))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_ADDRESS))
                val payment = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TASK_PAYMENT))
                val requester = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_USER))
                tasks.add(TaskAdapter.Task(id, title, desc, address, payment, requester))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks
    }

    fun getUserTasks(username: String): MutableList<TaskAdapter.Task> {
        val tasks = mutableListOf<TaskAdapter.Task>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS WHERE $COLUMN_TASK_USER=?", arrayOf(username))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESC))
                val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_ADDRESS))
                val payment = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TASK_PAYMENT))
                val requester = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_USER))
                tasks.add(TaskAdapter.Task(id, title, desc, address, payment, requester))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks
    }

    fun deleteTask(taskId: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_TASKS, "$COLUMN_TASK_ID=?", arrayOf(taskId.toString()))
    }
}