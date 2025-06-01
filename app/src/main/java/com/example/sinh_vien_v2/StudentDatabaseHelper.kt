package com.example.sinh_vien_v2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "students.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_MSSV = "mssv"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_MSSV TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_EMAIL TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Thêm sinh viên
    fun insertStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_MSSV, student.mssv)
            put(COLUMN_PHONE, student.phone)
            put(COLUMN_EMAIL, student.email)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Lấy tất cả sinh viên
//    fun getAllStudents(): List<Student> {
//        val list = mutableListOf<Student>()
//        val db = readableDatabase
//        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
//        while (cursor.moveToNext()) {
//            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
//            val mssv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MSSV))
//            val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
//            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
//            list.add(Student(name, mssv, phone, email))
//        }
//        cursor.close()
//        return list
//    }
    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, null, null, null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val mssv = cursor.getString(cursor.getColumnIndex(COLUMN_MSSV))
                val phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
                val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))

                students.add(Student(name, mssv, phone, email))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return students
    }


    // Cập nhật sinh viên theo MSSV (giả sử MSSV là duy nhất)
    fun updateStudent(student: Student): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_PHONE, student.phone)
            put(COLUMN_EMAIL, student.email)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_MSSV = ?",
            arrayOf(student.mssv)
        )
    }

    // Xoá sinh viên theo MSSV
//    fun deleteStudent(mssv: String): Int {
//        val db = writableDatabase
//        return db.delete(TABLE_NAME, "$COLUMN_MSSV = ?", arrayOf(mssv))
//    }
    fun deleteStudent(mssv: String): Int {
        val db = writableDatabase
        val selection = "$COLUMN_MSSV = ?"
        val selectionArgs = arrayOf(mssv)

        // Xoá sinh viên và trả về số dòng bị ảnh hưởng
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }

}
