package com.example.sinh_vien_v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Student(
    val name: String,
    val mssv: String,
    val phone: String = "",
    val email: String = ""
)

class StudentAdapter(
    private val students: MutableList<Student>,
    private val onOptionsClick: (student: Student, position: Int, anchorView: View) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStudent: TextView = itemView.findViewById(R.id.txtStudent)
        val txtMSSV: TextView = itemView.findViewById(R.id.txtMSSV)
        val btnOptions: ImageView = itemView.findViewById(R.id.btnOptions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.txtStudent.text = student.name
        holder.txtMSSV.text = "MSSV: ${student.mssv}"

        holder.btnOptions.setOnClickListener {
            onOptionsClick(student, position, holder.btnOptions)
        }
    }

    override fun getItemCount(): Int = students.size

    fun addStudent(student: Student) {
        students.add(student)
        notifyItemInserted(students.size - 1)
    }

    fun updateStudent(position: Int, newStudent: Student) {
        students[position] = newStudent
        notifyItemChanged(position)
    }

    fun removeStudent(position: Int) {
        students.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getStudent(position: Int): Student {
        return students[position]
    }
    // Phương thức để xóa tất cả sinh viên trong danh sách
    fun clearStudents() {
        students.clear()  // Xóa hết sinh viên trong danh sách
        notifyDataSetChanged()  // Cập nhật lại giao diện
    }

    fun addStudents(studentsList: List<Student>) {
        students.clear()  // Xoá dữ liệu cũ
        students.addAll(studentsList)  // Thêm tất cả sinh viên mới
        notifyDataSetChanged()  // Cập nhật giao diện
    }

}
