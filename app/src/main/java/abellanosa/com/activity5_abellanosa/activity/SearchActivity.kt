package abellanosa.com.activity5_abellanosa.activity

import abellanosa.com.activity5_abellanosa.R
import abellanosa.com.activity5_abellanosa.data.StudentDatabaseHandler
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    var dbHandler: StudentDatabaseHandler ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (isDatabaseEmpty()){
            txt_searchNoResult.text = getString(R.string.empty_database_message)
            showNoResult()
        }

        imgBtn_searchEdit.setOnClickListener {
            if(txt_searchStudentIDNum.text != "ID Number")
                editStudent()
        }

        imgBtn_searchDelete.setOnClickListener {
            if(txt_searchStudentIDNum.text != "ID Number")
                deleteStudent()
        }

        btn_searchSearch.setOnClickListener {
            if(edt_searchIdNumber.text.isNullOrEmpty()){
                Toast.makeText(this, "Please enter the ID number of the student", Toast.LENGTH_SHORT).show()
            }
            else{
                if (isDatabaseEmpty()){
                    txt_searchNoResult.text = getString(R.string.empty_database_message)
                    showNoResult()
                }
                else {
                    if (!dbHandler!!.isStudentExist(edt_searchIdNumber.text.toString().toInt())) {
                        showNoResult()
                    }
                    else {
                        var student = dbHandler!!.readStudent(edt_searchIdNumber.text.toString().toInt())
                        Log.d("Stud ID", student.idNumber.toString())

                        txt_searchStudentName.text = "${student.firstName} ${student.middleName} ${student.lastName}"
                        txt_searchStudentIDNum.text = student.idNumber.toString()
                        txt_searchStudentCourseYear.text = "${student.course} - ${student.yearLevel}"
                        showResult()
                    }
                }
            }
        }
    }

    private fun deleteStudent() {
        dbHandler!!.deleteStudent(txt_searchStudentIDNum.text.toString().toInt())
        showNoResult()
        Toast.makeText(this, "Student is removed from database", Toast.LENGTH_SHORT).show()
    }

    private fun editStudent() {

    }

    private fun showResult() {
        txt_searchNoResult.visibility = View.INVISIBLE
        txt_searchStudentCourseYear.visibility = View.VISIBLE
        txt_searchStudentIDNum.visibility = View.VISIBLE
        txt_searchStudentName.visibility = View.VISIBLE
        imgBtn_searchDelete.visibility = View.VISIBLE
        imgBtn_searchEdit.visibility = View.VISIBLE
        img_searchProfilePic.visibility = View.VISIBLE
    }

    fun showNoResult() {
        txt_searchNoResult.visibility = View.VISIBLE
        txt_searchStudentCourseYear.visibility = View.INVISIBLE
        txt_searchStudentIDNum.visibility = View.INVISIBLE
        txt_searchStudentName.visibility = View.INVISIBLE
        imgBtn_searchDelete.visibility = View.INVISIBLE
        imgBtn_searchEdit.visibility = View.INVISIBLE
        img_searchProfilePic.visibility = View.INVISIBLE
    }

    fun isDatabaseEmpty(): Boolean{
        dbHandler = StudentDatabaseHandler(this)
        return dbHandler!!.getStudentCount()==0
    }
}
