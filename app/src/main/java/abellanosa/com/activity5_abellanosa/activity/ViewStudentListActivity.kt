package abellanosa.com.activity5_abellanosa.activity

import abellanosa.com.activity5_abellanosa.R
import abellanosa.com.activity5_abellanosa.data.StudentDatabaseHandler
import abellanosa.com.activity5_abellanosa.data.StudentListAdapter
import abellanosa.com.activity5_abellanosa.model.Student
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view_student_list.*
import kotlinx.android.synthetic.main.popup_add_student.view.*

class ViewStudentListActivity : AppCompatActivity() {
    var dbHandler: StudentDatabaseHandler ?= null
    private var adapter: StudentListAdapter ?= null
    private var studentList: ArrayList<Student> ?= null
    private var studentListItems: ArrayList<Student> ?= null
    private var layoutManager: RecyclerView.LayoutManager ?= null
    private var dialogBuilder: AlertDialog.Builder ?= null
    private var dialog: AlertDialog ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_student_list)

        dbHandler= StudentDatabaseHandler(this)
        studentList = ArrayList<Student>()
        studentListItems = ArrayList()
        adapter = StudentListAdapter(studentListItems!!, this)
        layoutManager = LinearLayoutManager(this)

        if(dbHandler!!.getStudentCount()==0){
            txt_noRecord.visibility = View.VISIBLE
            rcv_studentListRecylerView.visibility = View.INVISIBLE
        }
        else{
            txt_noRecord.visibility = View.INVISIBLE
            rcv_studentListRecylerView.visibility = View.VISIBLE
            //setup list = recycler view
            rcv_studentListRecylerView.layoutManager = layoutManager
            rcv_studentListRecylerView.adapter = adapter

            //load up students
            studentList = dbHandler!!.readStudents()
            for (s in studentList!!.iterator()){
                var student = Student()
                student.idNumber = s.idNumber
                student.firstName = s.firstName
                student.middleName = s.middleName
                student.lastName = s.lastName
                student.course = s.course
                student.yearLevel = s.yearLevel

                studentListItems!!.add(student)
            }

            adapter!!.notifyDataSetChanged()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.btn_topMenuAdd){
            Log.d("MENU ITEM:", "MENU ITEM ADD CLICKED")
            createAddPopupDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    fun createAddPopupDialog(){
        var view = layoutInflater.inflate(R.layout.popup_add_student, null)
        var popupIdNum = view.edt_popupAddIdNumber
        var popupFirstName = view.edt_popupAddFirstName
        var popupMiddleName = view.edt_popupAddMiddleName
        var popupLastName = view.edt_popupAddLastName
        var popupCourse = view.edt_popupAddCourse
        var popupYearLevel = view.edt_popupAddYearLevel
        var popupEnrollBtn = view.btn_popupAddEnroll
        var popupCancelBtn = view.btn_popupAddCancel

        dialogBuilder = AlertDialog.Builder(this).setView(view)
        dialog = dialogBuilder!!.create()
        dialog?.show()

        popupCancelBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        popupEnrollBtn.setOnClickListener {
            if(popupIdNum.text.isNullOrEmpty() || popupFirstName.text.isNullOrEmpty() || popupMiddleName.text.isNullOrEmpty() || popupLastName.text.isNullOrEmpty() ||
                    popupCourse.text.isNullOrEmpty() || popupYearLevel.text.isNullOrEmpty()){
                Toast.makeText(this, "Please fill up the empty fields", Toast.LENGTH_SHORT).show()
            }
            else {
                var student = Student()

                student.idNumber = popupIdNum.text.toString().trim().toInt()
                student.firstName = popupFirstName.text.toString().trim()
                student.middleName = popupMiddleName.text.toString().trim()
                student.lastName = popupLastName.text.toString().trim()
                student.course = popupCourse.text.toString().trim()
                student.yearLevel = popupYearLevel.text.toString().trim().toInt()

                dbHandler!!.createStudent(student)
                Toast.makeText(this, "Student is now enrolled", Toast.LENGTH_SHORT).show()
                Log.d("SUCCESS:", "Student is added to database")
                dialog!!.dismiss()
                startActivity(Intent(this, ViewStudentListActivity::class.java))
                finish()
            }
        }

    }

}
