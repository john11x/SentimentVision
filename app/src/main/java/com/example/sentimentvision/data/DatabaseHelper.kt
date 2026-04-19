package com.example.sentimentvision.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "sentiment_vision.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE,
                password TEXT NOT NULL
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE sentiments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                sentiment TEXT,
                confidence REAL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE appointments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                date TEXT,
                notes TEXT,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS sentiments")
        db.execSQL("DROP TABLE IF EXISTS appointments")
        onCreate(db)
    }

    fun addUser(name: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }
        val result = db.insert("users", null, values)
        return result != -1L
    }

    fun addSentiment(userId: Int, sentiment: String, confidence: Double) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("sentiment", sentiment)
            put("confidence", confidence)
        }
        db.insert("sentiments", null, values)
    }

    fun getAllUsers(): List<User> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)
        val users = mutableListOf<User>()
        if (cursor.moveToFirst()) {
            do {
                users.add(
                    User(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

    fun getUserSentiments(userId: Int): List<SentimentEntry> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM sentiments WHERE user_id=? ORDER BY timestamp ASC",
            arrayOf(userId.toString())
        )
        val entries = mutableListOf<SentimentEntry>()
        if (cursor.moveToFirst()) {
            do {
                entries.add(
                    SentimentEntry(
                        sentiment = cursor.getString(cursor.getColumnIndexOrThrow("sentiment")),
                        confidence = cursor.getDouble(cursor.getColumnIndexOrThrow("confidence")),
                        timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return entries
    }

    fun addAppointment(userId: Int, date: String, notes: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("date", date)
            put("notes", notes)
        }
        db.insert("appointments", null, values)
    }
}

data class User(val id: Int, val name: String, val email: String)
data class SentimentEntry(val sentiment: String, val confidence: Double, val timestamp: String)
