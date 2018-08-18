package abellanosa.com.activity5_abellanosa.activity

import abellanosa.com.activity5_abellanosa.R
import abellanosa.com.activity5_abellanosa.data.StudentDatabaseHandler
import abellanosa.com.activity5_abellanosa.model.Student
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    var studentDatabaseHandler: StudentDatabaseHandler ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        studentDatabaseHandler = StudentDatabaseHandler(this)

        btn_addCancel.setOnClickListener { finish() }

        btn_addEnroll.setOnClickListener {
            if(edt_addIdNumber.text.isNullOrEmpty() || edt_addFirstName.text.isNullOrEmpty() || edt_addMiddleName.text.isNullOrEmpty() || edt_addLastName.text.isNullOrEmpty() ||
                    edt_addCourse.text.isNullOrEmpty() || edt_addYearLevel.text.isNullOrEmpty()){
                Toast.makeText(this, "Please fill up the empty fields", Toast.LENGTH_SHORT).show()
            }
            else {
                if(studentDatabaseHandler!!.isStudentExist(edt_addIdNumber.text.toString().trim().toInt())) {
                    Toast.makeText(this, "Sorry but this student is already enrolled", Toast.LENGTH_SHORT).show()
                }
                else {
                    saveToDataBase()
                    Toast.makeText(this, "Student is now enrolled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isEmptyDataBaseTable(): Boolean = studentDatabaseHandler!!.getStudentCount() == 0

    private fun saveToDataBase() {
        var student = Student()

        student.idNumber = edt_addIdNumber.text.toString().trim().toInt()
        student.firstName = edt_addFirstName.text.toString().trim()
        student.middleName = edt_addMiddleName.text.toString().trim()
        student.lastName = edt_addLastName.text.toString().trim()
        student.course = edt_addCourse.text.toString().trim()
        student.yearLevel = edt_addYearLevel.text.toString().trim().toInt()

        studentDatabaseHandler!!.createStudent(student)
        Log.d("SUCCESS:", "Student is added to database")
    }
}
