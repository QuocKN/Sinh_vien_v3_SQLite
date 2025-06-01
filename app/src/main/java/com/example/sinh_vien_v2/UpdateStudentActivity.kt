package com.example.sinh_vien_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateStudentActivity : AppCompatActivity() {
    private lateinit var dbHelper: StudentDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student) // Dùng lại layout

        dbHelper = StudentDatabaseHelper(this)  // Khởi tạo DB helper
        supportActionBar?.title = "Cập nhật sinh viên"

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtMSSV = findViewById<EditText>(R.id.edtMSSV)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val btnSave = findViewById<Button>(R.id.btnAdd)
        btnSave.text = "Cập nhật"

        // Lấy dữ liệu sinh viên từ intent
        val name = intent.getStringExtra("student_name") ?: ""
        val mssv = intent.getStringExtra("student_mssv") ?: ""
        val phone = intent.getStringExtra("student_phone") ?: ""
        val email = intent.getStringExtra("student_email") ?: ""
        val position = intent.getIntExtra("position", -1)

        edtName.setText(name)
        edtMSSV.setText(mssv)
        edtPhone.setText(phone)
        edtEmail.setText(email)

//        btnSave.setOnClickListener {
//            val newName = edtName.text.toString().trim()
//            val newMssv = edtMSSV.text.toString().trim()
//            val newPhone = edtPhone.text.toString().trim()
//            val newEmail = edtEmail.text.toString().trim()
//
//            if (newName.isNotEmpty() && newMssv.isNotEmpty()) {
//                val resultIntent = Intent().apply {
//                    putExtra("student_name", newName)
//                    putExtra("student_mssv", newMssv)
//                    putExtra("student_phone", newPhone)
//                    putExtra("student_email", newEmail)
//                    putExtra("position", position)
//                }
//                setResult(RESULT_OK, resultIntent)
//                finish()
//            } else {
//                Toast.makeText(this, "Vui lòng nhập tên và MSSV", Toast.LENGTH_SHORT).show()
//            }
//        }
        btnSave.setOnClickListener {
            val newName = edtName.text.toString().trim()
            val newMssv = edtMSSV.text.toString().trim()
            val newPhone = edtPhone.text.toString().trim()
            val newEmail = edtEmail.text.toString().trim()

            if (newName.isNotEmpty() && newMssv.isNotEmpty()) {
                val updatedStudent = Student(newName, newMssv, newPhone, newEmail)
                val resultIntent = Intent().apply {
                    putExtra("student_name", updatedStudent.name)
                    putExtra("student_mssv", updatedStudent.mssv)
                    putExtra("student_phone", updatedStudent.phone)
                    putExtra("student_email", updatedStudent.email)
                    putExtra("position", position)
                }

                // Cập nhật vào SQLite
                val updatedRows = dbHelper.updateStudent(updatedStudent)
                if (updatedRows > 0) {
                    Toast.makeText(this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng nhập tên và MSSV", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
