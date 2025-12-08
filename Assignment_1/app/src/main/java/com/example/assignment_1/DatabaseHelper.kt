package com.example.assignment_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ProjectDatabase.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_ROLE TEXT)")
        db?.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun insertUser(username: String, password: String, role: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_ROLE, role)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(username: String, password: String): String? {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_ROLE),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password), null, null, null)

        var role: String? = null
        if (cursor.moveToFirst()) {
            role = cursor.getString(0)
        }
        cursor.close()
        return role
    }
}