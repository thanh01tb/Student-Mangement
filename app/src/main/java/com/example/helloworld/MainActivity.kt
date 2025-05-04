package com.example.helloworld

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private val studentList = ArrayList<Student>()
    private val REQUEST_ADD = 1
    private val REQUEST_EDIT = 2
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView)

        adapter = StudentAdapter(studentList) { student, position, view ->
            selectedPosition = position
            showPopupMenu(student, view)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        studentList.add(Student("Nguyễn Văn A", "0123456789", "a@gmail.com" , "0123456789"))
        studentList.add(Student("Trần Thị B", "0987654321", "b@gmail.com" , "0123456789"))
        adapter.notifyDataSetChanged()

        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPopupMenu(student: Student, anchorView: android.view.View) {
        val popup = PopupMenu(this, anchorView)
        popup.menuInflater.inflate(R.menu.menu_student, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(this, AddStudentActivity::class.java)
                    intent.putExtra("student", student)
                    startActivityForResult(intent, REQUEST_EDIT)
                }
                R.id.menu_delete -> {
                    AlertDialog.Builder(this)
                        .setTitle("Xóa sinh viên")
                        .setMessage("Bạn có chắc muốn xóa sinh viên này?")
                        .setPositiveButton("Có") { _, _ ->
                            studentList.removeAt(selectedPosition)
                            adapter.notifyItemRemoved(selectedPosition)
                        }
                        .setNegativeButton("Không", null)
                        .show()
                }
                R.id.menu_call -> {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    callIntent.data = Uri.parse("tel:${student.phone}")
                    startActivity(callIntent)
                }
                R.id.menu_email -> {
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.data = Uri.parse("mailto:${student.email}")
                    startActivity(emailIntent)
                }
            }
            true
        }
        popup.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val student = data.getSerializableExtra("student") as? Student
            if (student != null) {
                if (requestCode == REQUEST_ADD) {
                    studentList.add(student)
                    adapter.notifyItemInserted(studentList.size - 1)
                } else if (requestCode == REQUEST_EDIT && selectedPosition != -1) {
                    studentList[selectedPosition] = student
                    adapter.notifyItemChanged(selectedPosition)
                }
            }
        }
    }
}
