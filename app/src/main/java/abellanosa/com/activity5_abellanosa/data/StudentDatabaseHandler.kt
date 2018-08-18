package abellanosa.com.activity5_abellanosa.data


import abellanosa.com.activity5_abellanosa.model.*
import android.app.ActionBar
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class StudentDatabaseHandler(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_STUDENTS_TABLE = "CREATE TABLE $TABLE_NAME($KEY_STUD_ID INTEGER PRIMARY KEY, $KEY_STUD_FIRST_NAME TEXT, $KEY_STUD_MIDDLE_NAME TEXT, $KEY_STUD_LAST_NAME TEXT, $KEY_STUD_COURSE TEXT, $KEY_STUD_YEAR_LEVEL INTEGER)"

        db?.execSQL(CREATE_STUDENTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        //CREATE TABLE AGAIN
        onCreate(db)
    }

    fun createStudent(student: Student){
        var db: SQLiteDatabase = writableDatabase
        var values = ContentValues()

        values.put(KEY_STUD_ID, student.idNumber)
        values.put(KEY_STUD_FIRST_NAME, student.firstName)
        values.put(KEY_STUD_MIDDLE_NAME, student.middleName)
        values.put(KEY_STUD_LAST_NAME, student.lastName)
        values.put(KEY_STUD_COURSE, student.course)
        values.put(KEY_STUD_YEAR_LEVEL, student.yearLevel)

        db.insert(TABLE_NAME, null, values)
        Log.d("DATA INSERTED", "SUCCESS")
        db.close()
    }

    fun readStudents(): ArrayList<Student>{
        var db: SQLiteDatabase = writableDatabase
        var cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        var result = ArrayList<Student>()

        if(cursor != null){
            cursor.moveToFirst()
        }

        do {
            var student = Student()

            student.idNumber = cursor.getInt(cursor.getColumnIndex(KEY_STUD_ID))
            student.firstName = cursor.getString(cursor.getColumnIndex(KEY_STUD_FIRST_NAME))
            student.middleName = cursor.getString(cursor.getColumnIndex(KEY_STUD_MIDDLE_NAME))
            student.lastName = cursor.getString(cursor.getColumnIndex(KEY_STUD_LAST_NAME))
            student.course = cursor.getString(cursor.getColumnIndex(KEY_STUD_COURSE))
            student.yearLevel = cursor.getInt(cursor.getColumnIndex(KEY_STUD_YEAR_LEVEL))

            result.add(student)
        }while (cursor.moveToNext())

        cursor.close()
        db.close()

        return  result
    }

    fun isStudentExist(studID: Int): Boolean{
        var db: SQLiteDatabase = writableDatabase
        var cursor: Cursor = db.query(TABLE_NAME, arrayOf(KEY_STUD_ID), "$KEY_STUD_ID=?", arrayOf(studID.toString()), null, null, null, null)

        if(cursor != null){

            cursor.moveToFirst()
        }

        var result = cursor.count != 0

        cursor.close()
        db.close()

        return result
    }

    fun readStudent(studID: Int): Student{
        var db: SQLiteDatabase = writableDatabase
//        var cursor: Cursor = db.query(TABLE_NAME, arrayOf(KEY_ID, KEY_CHORE_NAME, KEY_CHORE_ASSIGNED_BY, KEY_CHORE_ASSIGNED_TIME, KEY_CHORE_ASSIGNED_TO), "$KEY_ID=?", arrayOf(id.toString()), null, null, null, null)
        var cursor: Cursor = db.query(TABLE_NAME, arrayOf(KEY_STUD_ID, KEY_STUD_FIRST_NAME, KEY_STUD_MIDDLE_NAME, KEY_STUD_LAST_NAME, KEY_STUD_COURSE, KEY_STUD_YEAR_LEVEL),
                "$KEY_STUD_ID=?", arrayOf(studID.toString()), null, null, null, null)

        if(cursor != null){
            cursor.moveToFirst()
        }

        var student = Student()

        student.idNumber = cursor.getInt(cursor.getColumnIndex(KEY_STUD_ID))
        student.firstName = cursor.getString(cursor.getColumnIndex(KEY_STUD_FIRST_NAME))
        student.middleName = cursor.getString(cursor.getColumnIndex(KEY_STUD_MIDDLE_NAME))
        student.lastName = cursor.getString(cursor.getColumnIndex(KEY_STUD_LAST_NAME))
        student.course = cursor.getString(cursor.getColumnIndex(KEY_STUD_COURSE))
        student.yearLevel = cursor.getInt(cursor.getColumnIndex(KEY_STUD_YEAR_LEVEL))

        cursor.close()
        db.close()

        return  student
    }

    fun updateStudent(student: Student, id: Int): Int{
        var db: SQLiteDatabase = writableDatabase
        var values = ContentValues()

        values.put(KEY_STUD_ID, student.idNumber)
        values.put(KEY_STUD_FIRST_NAME, student.firstName)
        values.put(KEY_STUD_MIDDLE_NAME, student.middleName)
        values.put(KEY_STUD_LAST_NAME, student.lastName)
        values.put(KEY_STUD_COURSE, student.course)
        values.put(KEY_STUD_YEAR_LEVEL, student.yearLevel)

        //update rows
        return db.update(TABLE_NAME, values, "$KEY_STUD_ID=?", arrayOf(id.toString()))
    }

    fun deleteStudent(id: Int){
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, "$KEY_STUD_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getStudentCount():Int{
        var db: SQLiteDatabase = readableDatabase
        var countQuery = "Select * from $TABLE_NAME"

        var cursor: Cursor = db.rawQuery(countQuery, null)
        var count = cursor.count

        cursor.close()
        db.close()
        return count
    }

}