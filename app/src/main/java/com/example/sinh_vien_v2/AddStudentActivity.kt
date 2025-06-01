package com.example.sinh_vien_v2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        supportActionBar?.title = "Thêm Sinh Viên"

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtMSSV = findViewById<EditText>(R.id.edtMSSV)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val btnSave = findViewById<Button>(R.id.btnAdd)

        btnSave.setOnClickListener {
            val name = edtName.text.toString().trim()
            val mssv = edtMSSV.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val email = edtEmail.text.toString().trim()


            if (name.isNotEmpty() && mssv.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("student_name", name)
                    putExtra("student_mssv", mssv)
                    putExtra("student_phone", phone)
                    putExtra("student_email", email)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
