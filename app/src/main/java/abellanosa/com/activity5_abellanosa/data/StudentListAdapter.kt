package abellanosa.com.activity5_abellanosa.data

import abellanosa.com.activity5_abellanosa.R
import abellanosa.com.activity5_abellanosa.model.Student
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.popup_add_student.view.*

class StudentListAdapter(private  val list: ArrayList<Student>, private val context: Context): RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var studentName= itemView.findViewById<TextView>(R.id.txt_listRowStudentFullName)
        var studentIdNum= itemView.findViewById<TextView>(R.id.txt_listRowIDNumber)
        var studentCourseYear= itemView.findViewById<TextView>(R.id.txt_listRowCourseYear)
        var studentEdit = itemView.findViewById<ImageButton>(R.id.imgbtn_listRowEdit)
        var studentDelete = itemView.findViewById<ImageButton>(R.id.imgbtn_listRowDelete)

        fun bindViews(student: Student){
            studentName.text = "${student.firstName} ${student.middleName} ${student.lastName}"
            studentIdNum.text = student.idNumber.toString()
            studentCourseYear.text = "${student.course} - ${student.yearLevel}"

            studentEdit.setOnClickListener(this)
            studentDelete.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            var currentPosition: Int = adapterPosition
            var currentStudent = list[currentPosition]

            when(v!!.id){
                studentDelete.id -> {
                    deleteStudent(currentStudent.idNumber!!.toInt())
                    list.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    Toast.makeText(context, "Student successfully deleted", Toast.LENGTH_SHORT).show()
                }
                studentEdit.id -> {
                    editStudent(currentStudent)


                }
            }
        }

        fun deleteStudent(id: Int){
            var db = StudentDatabaseHandler(context)
            db.deleteStudent(id)
        }

        fun editStudent(student: Student){
            var dialogBuilder: AlertDialog.Builder?
            var dialog: AlertDialog?
            var db = StudentDatabaseHandler(context)

            var view = LayoutInflater.from(context).inflate(R.layout.popup_add_student, null)

            var popupIdNum = view.edt_popupAddIdNumber
            var popupFirstName = view.edt_popupAddFirstName
            var popupMiddleName = view.edt_popupAddMiddleName
            var popupLastName = view.edt_popupAddLastName
            var popupCourse = view.edt_popupAddCourse
            var popupYearLevel = view.edt_popupAddYearLevel
            var popupEnrollBtn = view.btn_popupAddEnroll
            var popupCancelBtn = view.btn_popupAddCancel

            dialogBuilder = AlertDialog.Builder(context).setView(view)
            dialog = dialogBuilder!!.create()
            dialog?.show()

            popupCancelBtn.setOnClickListener {
                dialog!!.dismiss()
            }

            popupEnrollBtn.setOnClickListener {
                if(popupIdNum.text.isNullOrEmpty() || popupFirstName.text.isNullOrEmpty() || popupMiddleName.text.isNullOrEmpty() || popupLastName.text.isNullOrEmpty() ||
                        popupCourse.text.isNullOrEmpty() || popupYearLevel.text.isNullOrEmpty()){
                    Toast.makeText(context, "Please fill up the empty fields", Toast.LENGTH_SHORT).show()
                }
                else {
                    student.idNumber = popupIdNum.text.toString().trim().toInt()
                    student.firstName = popupFirstName.text.toString().trim()
                    student.middleName = popupMiddleName.text.toString().trim()
                    student.lastName = popupLastName.text.toString().trim()
                    student.course = popupCourse.text.toString().trim()
                    student.yearLevel = popupYearLevel.text.toString().trim().toInt()

                    db.updateStudent(student, student.idNumber!!.toInt())
                    notifyItemChanged(adapterPosition, student)

                    Toast.makeText(context, "Student successfully updated", Toast.LENGTH_SHORT).show()
                    Log.d("SUCCESS:", "Student is updated to database")
                    dialog!!.dismiss()
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.student_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: StudentListAdapter.ViewHolder, position: Int) {
        holder.bindViews(list[position])
    }
}