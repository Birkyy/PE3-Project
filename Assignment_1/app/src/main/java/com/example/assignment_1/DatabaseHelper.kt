package com.example.assignment_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ProjectDatabase.db"
        private const val DATABASE_VERSION = 3
        const val TABLE_USERS = "users"

        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
        const val COLUMN_FULLNAME = "fullname"
        const val TABLE_COURSES = "courses"
        const val COLUMN_COURSE_ID = "course_id"
        const val COLUMN_COURSE_TITLE = "title"
        const val COLUMN_COURSE_DESC = "description"
        const val COLUMN_COURSE_PRICE = "price"
        const val COLUMN_COURSE_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_FULLNAME TEXT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_ROLE TEXT)")
        db?.execSQL(createUsersTable)

        val createCoursesTable = ("CREATE TABLE $TABLE_COURSES (" +
                "$COLUMN_COURSE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_COURSE_TITLE TEXT, " +
                "$COLUMN_COURSE_DESC TEXT, " +
                "$COLUMN_COURSE_PRICE REAL, " +
                "$COLUMN_COURSE_IMAGE BLOB)")


        db?.execSQL(createCoursesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        onCreate(db)
    }

    fun insertUser(fullname: String, username: String, password: String, role: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FULLNAME, fullname)
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

    fun checkUsernameExists(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USERNAME),
            "$COLUMN_USERNAME = ?",
            arrayOf(username), null, null, null)

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertCourse(title: String, desc: String, price: Double, imageBytes: ByteArray): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COURSE_TITLE, title)
            put(COLUMN_COURSE_DESC, desc)
            put(COLUMN_COURSE_PRICE, price)
            put(COLUMN_COURSE_IMAGE, imageBytes)
        }
        return db.insert(TABLE_COURSES, null, values)
    }

    fun getAllCourses(): List<Course> {
        val courseList = mutableListOf<Course>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)).toString()
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TITLE))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESC))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))
                val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_COURSE_IMAGE))
                val bitmap = ImageUtil.getBitmapFromBytes(imageBytes)

                courseList.add(Course(id, title, "You", desc, price, bitmap))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courseList
    }

    fun updateCourse(id: String, title: String, desc: String, price: Double, imageBytes: ByteArray): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COURSE_TITLE, title)
            put(COLUMN_COURSE_DESC, desc)
            put(COLUMN_COURSE_PRICE, price)
            put(COLUMN_COURSE_IMAGE, imageBytes)
        }
        return db.update(TABLE_COURSES, values, "$COLUMN_COURSE_ID = ?", arrayOf(id))
    }

    fun deleteCourse(id: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_COURSES, "$COLUMN_COURSE_ID = ?", arrayOf(id))
    }

    fun getAllTutors(): List<Tutor> {
        val tutorList = mutableListOf<Tutor>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_ROLE = ?", arrayOf("Tutor"))

        val idIndex = cursor.getColumnIndex(COLUMN_USER_ID)
        val nameIndex = cursor.getColumnIndex(COLUMN_FULLNAME)

        val defaultBitmap = ImageUtil.drawableToBitmap(context, R.drawable.placeholder_img)

        if (cursor.moveToFirst()) {
            do {
                if (idIndex != -1 && nameIndex != -1) {
                    val id = cursor.getInt(idIndex).toString()
                    val name = cursor.getString(nameIndex)
                    tutorList.add(Tutor(id, name, defaultBitmap))
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return tutorList
    }
}