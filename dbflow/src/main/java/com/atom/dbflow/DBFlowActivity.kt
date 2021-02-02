package com.atom.dbflow

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.atom.other.UserOther
import com.raizlabs.android.dbflow.config.DBFlowDatabaseGeneratedDatabaseHolder
import com.raizlabs.android.dbflow.config.DBFlowOtherDatabaseGeneratedDatabaseHolder
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.saop.annotation.AopPermissionVoid
import java.io.File
import java.util.*

class DBFlowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbflow)

        findViewById<Button>(R.id.init_db).setOnClickListener {
            createDb()
        }
        findViewById<Button>(R.id.recycle_db).setOnClickListener {
            recycleDb()
        }
        findViewById<Button>(R.id.db_insert).setOnClickListener {
            val input = findViewById<EditText>(R.id.input).text.toString()
            User().apply {
                this.setId(UUID.randomUUID().toString())
                this.setName("主module $input + ${System.currentTimeMillis()}")
                Log.e("DBFlowActivity", toString())
            }.save()

            UserOther().apply {
                this.setId(UUID.randomUUID().toString())
                this.setName("子module $input + ${System.currentTimeMillis()}")
                Log.e("DBFlowActivity", toString())
            }.save()
        }
        findViewById<Button>(R.id.db_query).setOnClickListener {
            val queryList = SQLite.select().from(User::class).limit(100).queryList()
            queryList.forEach {
                Log.e("DBFlowActivity", it.toString())
            }
            val queryList2 = SQLite.select().from(UserOther::class).limit(100).queryList()
            queryList2.forEach {
                Log.e("DBFlowActivity", it.toString())
            }
        }
        findViewById<Button>(R.id.db_delete).setOnClickListener {

        }
    }

    @AopPermissionVoid(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private fun createDb() {
        val input = findViewById<EditText>(R.id.input).text.toString()
        sdCardIsAvailable()?.also {
            val dbFile = File(it, input)
            FlowManager.init(
                FlowConfig.Builder(DBFlowContext.initDB(this, dbFile))
                    .addDatabaseHolder(DBFlowDatabaseGeneratedDatabaseHolder::class.java)
                    .addDatabaseHolder(DBFlowOtherDatabaseGeneratedDatabaseHolder::class.java)
                    .build()
            )
        }
    }

    @AopPermissionVoid(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private fun recycleDb() {
        FlowManager.destroy()
    }

    fun sdCardIsAvailable(): File? {
        //首先判断外部存储是否可用
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            File(Environment.getExternalStorageDirectory().path)
        } else {
            null
        }
    }
}