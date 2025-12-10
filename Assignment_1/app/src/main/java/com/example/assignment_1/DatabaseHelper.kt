package com.example.assignment_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ProjectDatabase.db"
        private const val DATABASE_VERSION = 11
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
        const val COLUMN_FULLNAME = "fullname"
        const val TABLE_COURSES = "courses"
        const val COLUMN_COURSE_ID = "course_id"
        const val COLUMN_COURSE_TITLE = "title"
        const val COLUMN_COURSE_AUTHOR = "author"
        const val COLUMN_COURSE_DESC = "description"
        const val COLUMN_COURSE_PRICE = "price"
        const val COLUMN_COURSE_IMAGE = "image"
        const val TABLE_SESSIONS = "sessions"
        const val COLUMN_SESSION_ID = "session_id"
        const val COLUMN_SESSION_TUTOR = "tutor_name"
        const val COLUMN_SESSION_STUDENT = "student_name"
        const val COLUMN_SESSION_SUBJECT = "subject"
        const val COLUMN_SESSION_DATE = "date"
        const val COLUMN_SESSION_TIME = "time"
        const val COLUMN_SESSION_STATUS = "status"
        const val COLUMN_SESSION_DURATION = "duration"
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
                "$COLUMN_COURSE_AUTHOR TEXT, " +
                "$COLUMN_COURSE_DESC TEXT, " +
                "$COLUMN_COURSE_PRICE REAL, " +
                "$COLUMN_COURSE_IMAGE BLOB)")
        db?.execSQL(createCoursesTable)

        val createSessionsTable = ("CREATE TABLE $TABLE_SESSIONS (" +
                "$COLUMN_SESSION_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_SESSION_TUTOR TEXT, " +
                "$COLUMN_SESSION_STUDENT TEXT, " +
                "$COLUMN_SESSION_SUBJECT TEXT, " +
                "$COLUMN_SESSION_DATE TEXT, " +
                "$COLUMN_SESSION_TIME TEXT, " +
                "$COLUMN_SESSION_DURATION INTEGER, " +
                "$COLUMN_SESSION_STATUS TEXT)")
        db?.execSQL(createSessionsTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SESSIONS")
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

    fun insertCourse(title: String, author: String, desc: String, price: Double, imageBytes: ByteArray): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COURSE_TITLE, title)
            put(COLUMN_COURSE_AUTHOR, author)
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
                val author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_AUTHOR))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESC))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))
                val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_COURSE_IMAGE))
                val bitmap = ImageUtil.getBitmapFromBytes(imageBytes)

                courseList.add(Course(id, title, author, desc, price, bitmap))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courseList
    }

    fun getCourseById(id: String): Course? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSES WHERE $COLUMN_COURSE_ID = ?", arrayOf(id))

        var course: Course? = null
        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TITLE))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_AUTHOR))
            val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESC))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))
            val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_COURSE_IMAGE))

            val bitmap = ImageUtil.getBitmapFromBytes(imageBytes)

            course = Course(id, title, author, desc, price, bitmap)
        }
        cursor.close()
        return course
    }

    fun getCoursesByAuthor(authorName: String): List<Course> {
        val courseList = mutableListOf<Course>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSES WHERE $COLUMN_COURSE_AUTHOR = ?", arrayOf(authorName))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)).toString()
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TITLE))
                val author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_AUTHOR))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESC))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))
                val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_COURSE_IMAGE))

                val bitmap = ImageUtil.getBitmapFromBytes(imageBytes)

                courseList.add(Course(id, title, author, desc, price, bitmap))
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

    fun getUserFullName(username: String): String {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_FULLNAME),
            "$COLUMN_USERNAME = ?", arrayOf(username), null, null, null)

        var fullName = "Unknown Tutor"
        if (cursor.moveToFirst()) {
            // We use the constant we defined earlier
            val index = cursor.getColumnIndex(COLUMN_FULLNAME)
            if (index != -1) {
                fullName = cursor.getString(index)
            }
        }
        cursor.close()
        return fullName
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

    fun bookSession(tutorName: String, studentName: String, subject: String, date: String, time: String, duration: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SESSION_TUTOR, tutorName)
            put(COLUMN_SESSION_STUDENT, studentName)
            put(COLUMN_SESSION_SUBJECT, subject)
            put(COLUMN_SESSION_DATE, date)
            put(COLUMN_SESSION_TIME, time)
            put(COLUMN_SESSION_DURATION, duration)
            put(COLUMN_SESSION_STATUS, "Pending")
        }
        return db.insert(TABLE_SESSIONS, null, values)
    }

    private fun getCoursePrice(title: String): Double {
        val db = readableDatabase
        val cursor = db.query(TABLE_COURSES, arrayOf(COLUMN_COURSE_PRICE),
            "$COLUMN_COURSE_TITLE = ?", arrayOf(title), null, null, null)

        var price = 0.0
        if (cursor.moveToFirst()) {
            price = cursor.getDouble(0)
        }
        cursor.close()
        return price
    }

    fun calculateTutorEarnings(tutorName: String): Double {
        var totalEarnings = 0.0
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_SESSIONS WHERE $COLUMN_SESSION_TUTOR = ?", arrayOf(tutorName))

        if (cursor.moveToFirst()) {
            do {
                val subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_SUBJECT))
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SESSION_DURATION))
                val price = getCoursePrice(subject)

                totalEarnings += (price * duration)

            } while (cursor.moveToNext())
        }
        cursor.close()
        return totalEarnings
    }

    fun getStudentSessions(studentName: String, isUpcoming: Boolean): List<Session> {
        val sessionList = mutableListOf<Session>()
        val db = readableDatabase

        val selection = if (isUpcoming) {
            "$COLUMN_SESSION_STUDENT = ? AND ($COLUMN_SESSION_STATUS = 'Pending' OR $COLUMN_SESSION_STATUS = 'Confirmed')"
        } else {
            "$COLUMN_SESSION_STUDENT = ? AND ($COLUMN_SESSION_STATUS = 'Completed' OR $COLUMN_SESSION_STATUS = 'Cancelled')"
        }

        val cursor = db.query(TABLE_SESSIONS, null, selection, arrayOf(studentName), null, null, "$COLUMN_SESSION_DATE DESC")

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SESSION_ID)).toString()
                val tutor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_TUTOR))
                val subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_SUBJECT))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_DATE))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_TIME))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_STATUS))
                val student = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_STUDENT))

                sessionList.add(Session(id, tutor, subject, date, time, status, student))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return sessionList
    }

    fun getTutorSessions(tutorName: String, isUpcoming: Boolean): List<Session> {
        val sessionList = mutableListOf<Session>()
        val db = readableDatabase

        val selection = if (isUpcoming) {
            "$COLUMN_SESSION_TUTOR = ? AND ($COLUMN_SESSION_STATUS = 'Pending' OR $COLUMN_SESSION_STATUS = 'Confirmed')"
        } else {
            "$COLUMN_SESSION_TUTOR = ? AND ($COLUMN_SESSION_STATUS = 'Completed' OR $COLUMN_SESSION_STATUS = 'Cancelled')"
        }

        val cursor = db.query(TABLE_SESSIONS, null, selection, arrayOf(tutorName), null, null, "$COLUMN_SESSION_DATE DESC")

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SESSION_ID)).toString()
                val tutor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_TUTOR))
                val subject = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_SUBJECT))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_DATE))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_TIME))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_STATUS))
                val student = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SESSION_STUDENT))

                sessionList.add(Session(id, tutor, subject, date, time, status, student))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return sessionList
    }

    fun updateSessionStatus(sessionId: String, newStatus: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SESSION_STATUS, newStatus)
        }
        val rowsAffected = db.update(TABLE_SESSIONS, values, "$COLUMN_SESSION_ID = ?", arrayOf(sessionId))
        return rowsAffected > 0
    }
}