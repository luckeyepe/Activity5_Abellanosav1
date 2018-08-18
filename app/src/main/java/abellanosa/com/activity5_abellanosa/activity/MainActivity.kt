package abellanosa.com.activity5_abellanosa.activity

import abellanosa.com.activity5_abellanosa.R
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_addStudent.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        btn_viewRecord.setOnClickListener { startActivity(Intent(this, ViewStudentListActivity::class.java)) }

        btn_searchStudent.setOnClickListener { startActivity(Intent(this, SearchActivity::class.java)) }
    }
}
