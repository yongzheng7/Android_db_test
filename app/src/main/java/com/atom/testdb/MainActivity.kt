package com.atom.testdb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.atom.dbflow.DBFlowActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.dbflow_test)
                .setOnClickListener(View.OnClickListener {
                   startActivity(Intent(this , DBFlowActivity::class.java))
                })
    }
}