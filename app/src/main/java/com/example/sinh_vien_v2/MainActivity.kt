package com.example.sinh_vien_v2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val studentList = mutableListOf<Student>()
    private lateinit var studentAdapter: StudentAdapter
    private val ADD_STUDENT_REQUEST = 100
    private val UPDATE_STUDENT_REQUEST = 101
    private lateinit var dbHelper: StudentDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Danh sách sinh viên"
        dbHelper = StudentDatabaseHelper(this) // Khởi tạo DB helper
        studentList.addAll(dbHelper.getAllStudents()) // Tải dữ liệu từ DB

        val recyclerView = findViewById<RecyclerView>(R.id.list_students)
        studentAdapter = StudentAdapter(studentList) { student, position, anchorView ->
            showPopupMenu(student, position, anchorView)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter

        loadStudentsFromDatabase()
    }

    // Phương thức để tải danh sách sinh viên từ SQLite
    private fun loadStudentsFromDatabase() {
        val students = dbHelper.getAllStudents()  // Lấy danh sách sinh viên từ DB
        studentAdapter.clearStudents()  // Xoá danh sách sinh viên hiện tại trong adapter
        studentAdapter.addStudents(students)  // Thêm sinh viên mới vào adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_student) {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivityForResult(intent, ADD_STUDENT_REQUEST)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPopupMenu(student: Student, position: Int, anchorView: View) {
        val popup = PopupMenu(this, anchorView)
        popup.menuInflater.inflate(R.menu.menu_student, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_update -> {
                    val intent = Intent(this, UpdateStudentActivity::class.java).apply {
                        putExtra("student_name", student.name)
                        putExtra("student_mssv", student.mssv)
                        putExtra("student_phone", student.phone)
                        putExtra("student_email", student.email)
                        putExtra("position", position)
                    }
                    startActivityForResult(intent, UPDATE_STUDENT_REQUEST)
                }

//                R.id.menu_delete -> {
//                    AlertDialog.Builder(this)
//                        .setTitle("Xác nhận xóa")
//                        .setMessage("Bạn có chắc muốn xóa sinh viên này?")
//                        .setPositiveButton("Xóa") { _, _ ->
//                            studentAdapter.removeStudent(position)
//                        }
//                        .setNegativeButton("Hủy", null)
//                        .show()
//                }
                R.id.menu_delete -> {
                    AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sinh viên này?")
                        .setPositiveButton("Xóa") { _, _ ->
                            // Xoá sinh viên khỏi SQLite
                            val student = studentAdapter.getStudent(position)
                            val deletedRows = dbHelper.deleteStudent(student.mssv)
                            if (deletedRows > 0) {
                                // Xoá sinh viên khỏi adapter
                                studentAdapter.removeStudent(position)
                                Toast.makeText(this, "Đã xóa sinh viên", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Lỗi khi xóa sinh viên", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                }


                R.id.menu_call -> {
                    if (student.phone.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${student.phone}")
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Không có số điện thoại", Toast.LENGTH_SHORT).show()
                    }
                }

                R.id.menu_email -> {
                    if (student.email.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${student.email}")
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Không có email", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            true
        }

        popup.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val name = data.getStringExtra("student_name") ?: return
            val mssv = data.getStringExtra("student_mssv") ?: return
            val phone = data.getStringExtra("student_phone") ?: ""
            val email = data.getStringExtra("student_email") ?: ""
            val newStudent = Student(name, mssv, phone, email)

            when (requestCode) {
//                ADD_STUDENT_REQUEST -> studentAdapter.addStudent(newStudent)
                ADD_STUDENT_REQUEST -> {
                    val id = dbHelper.insertStudent(newStudent)
                    if (id != -1L) {
                        studentAdapter.addStudent(newStudent)
                        Toast.makeText(this, "Đã lưu vào SQLite", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Lỗi lưu dữ liệu", Toast.LENGTH_SHORT).show()
                    }
                }

//                UPDATE_STUDENT_REQUEST -> {
//                    val position = data.getIntExtra("position", -1)
//                    if (position != -1) {
//                        studentAdapter.updateStudent(position, newStudent)
//                    }
//                }
                UPDATE_STUDENT_REQUEST -> {
                    val position = data.getIntExtra("position", -1)
                    if (position != -1) {
                        val updatedStudent = Student(
                            data.getStringExtra("student_name") ?: "",
                            data.getStringExtra("student_mssv") ?: "",
                            data.getStringExtra("student_phone") ?: "",
                            data.getStringExtra("student_email") ?: ""
                        )

                        studentAdapter.updateStudent(position, updatedStudent)
                    }
                }

            }
        }
    }
}
