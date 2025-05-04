package com.example.helloworld

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtMssv = findViewById<EditText>(R.id.edtMSSV)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val btnSave = findViewById<Button>(R.id.btnSave)

        val oldStudent = intent.getSerializableExtra("student") as? Student
        oldStudent?.let {
            edtName.setText(it.name)
            edtMssv.setText(it.mssv)
            edtEmail.setText(it.email)
            edtPhone.setText(it.phone)
        }

        btnSave.setOnClickListener {
            val student = Student(
                edtName.text.toString(),
                edtMssv.text.toString(),
                edtEmail.text.toString(),
                edtPhone.text.toString()
            )
            val intent = Intent()
            intent.putExtra("student", student)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}