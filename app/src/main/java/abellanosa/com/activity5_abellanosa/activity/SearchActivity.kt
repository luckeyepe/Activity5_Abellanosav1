package abellanosa.com.activity5_abellanosa.activity

import abellanosa.com.activity5_abellanosa.R
import abellanosa.com.activity5_abellanosa.data.StudentDatabaseHandler
import abellanosa.com.activity5_abellanosa.model.Student
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.popup_add_student.view.*

class SearchActivity : AppCompatActivity() {
    private var dbHandler: StudentDatabaseHandler ?= null
    private var student: Student?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (isDatabaseEmpty()){
            txt_searchNoResult.text = getString(R.string.empty_database_message)
            showNoResult()
        }

        imgBtn_searchEdit.setOnClickListener {
            //check if student is in tha database
            if(txt_searchStudentIDNum.text != "ID Number")
                editStudent()
        }

        imgBtn_searchDelete.setOnClickListener {
            //check if the student is in the database
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
                        findAndDisplayStudent()
                    }
                }
            }
        }
    }

    fun findAndDisplayStudent() {
        student = dbHandler!!.readStudent(edt_searchIdNumber.text.toString().toInt())
        Log.d("Stud ID", student!!.idNumber.toString())

        txt_searchStudentName.text = "${student!!.firstName} ${student!!.middleName} ${student!!.lastName}"
        txt_searchStudentIDNum.text = student!!.idNumber.toString()
        txt_searchStudentCourseYear.text = "${student!!.course} - ${student!!.yearLevel}"
        showResult()
    }

    private fun deleteStudent() {
        dbHandler!!.deleteStudent(txt_searchStudentIDNum.text.toString().toInt())
        showNoResult()
        Toast.makeText(this, "Student is removed from database", Toast.LENGTH_SHORT).show()
    }

    private fun editStudent() {
        var dialogBuilder: AlertDialog.Builder?
        var dialog: AlertDialog?
        var db = StudentDatabaseHandler(this)

        var view = LayoutInflater.from(this).inflate(R.layout.popup_add_student, null)

        var popupIdNum = view.edt_popupAddIdNumber
        var popupFirstName = view.edt_popupAddFirstName
        var popupMiddleName = view.edt_popupAddMiddleName
        var popupLastName = view.edt_popupAddLastName
        var popupCourse = view.edt_popupAddCourse
        var popupYearLevel = view.edt_popupAddYearLevel
        var popupEnrollBtn = view.btn_popupAddEnroll
        var popupCancelBtn = view.btn_popupAddCancel

        //fill up the edittexts with hints about the current student info
        popupIdNum.hint = student!!.idNumber.toString()
        popupFirstName.hint = student!!.firstName
        popupMiddleName.hint = student!!.middleName
        popupLastName.hint = student!!.lastName
        popupCourse.hint = student!!.course
        popupYearLevel.hint = student!!.yearLevel.toString()

        //make edit text for id num uneditable for safety reasons
        popupIdNum.isEnabled = false

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog?.show()

        popupCancelBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        popupEnrollBtn.setOnClickListener {
            if(!popupFirstName.text.isNullOrEmpty()){
                student!!.firstName = popupFirstName.text.toString().trim()
            }

            if(!popupMiddleName.text.isNullOrEmpty()){
                student!!.middleName = popupMiddleName.text.toString().trim()
            }

            if(!popupLastName.text.isNullOrEmpty()){
                student!!.lastName = popupLastName.text.toString().trim()
            }

            if(!popupCourse.text.isNullOrEmpty()){
                student!!.course = popupCourse.text.toString().trim()
            }

            if(!popupYearLevel.text.isNullOrEmpty()){
                student!!.yearLevel = popupYearLevel.text.toString().trim().toInt()
            }

            db.updateStudent(student!!, student!!.idNumber!!.toInt())

            Toast.makeText(this, "Student successfully updated", Toast.LENGTH_SHORT).show()
            Log.d("SUCCESS:", "Student is updated to database")
            dialog!!.dismiss()

            findAndDisplayStudent()
        }
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
