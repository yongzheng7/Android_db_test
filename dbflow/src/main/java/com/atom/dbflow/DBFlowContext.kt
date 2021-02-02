package com.atom.dbflow

import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.raizlabs.android.dbflow.config.FlowLog
import com.raizlabs.android.dbflow.config.FlowManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class DBFlowContext : ContextWrapper {

    companion object{
        //初始化数据库
        fun initDB(context: Context , file : File):ContextWrapper {
            return DBFlowContext(context, file, true)
        }

        //重新加载数据库
        fun deleteDB(context: Context , file : File) {
            file.delete()
            // 释放引用，才能重新创建表
            FlowManager.destroy()
        }
    }

    private var file: File ?= null
    private var isUseDefaultDB = false
    private val context: Context

    constructor(
        base: Context,
        file: File?,
        isUseDefaultDB: Boolean
    ) : super(base) {
        this.file = file
        this.context = base
        this.isUseDefaultDB = isUseDefaultDB
    }

    override fun getApplicationContext(): Context {
        return this
    }

    override fun getDatabasePath(name: String): File {
        if (isUseDefaultDB) {
            var tempFile = file
            if (tempFile == null) {
                tempFile = context.getDatabasePath(name).parentFile
            }
            createInitDatabase(tempFile!!, name, context)
        }

        if (file == null) return context.getDatabasePath(name)

        // 判断目录是否存在，不存在则创建该目录
        if (!file!!.exists()) file!!.mkdirs()

        var addressFile: File? = File(file, name)
        if (addressFile == null) addressFile = context.getDatabasePath(name)
        return addressFile!!
    }

    private fun createInitDatabase(
        toFile: File,
        name: String,
        context: Context
    ): Boolean {
        val path = toFile.absolutePath
        if (TextUtils.isEmpty(path)) return false
        // 判断数据文件是否存在，不存在的话使用打包的数据文件
        val toFileName = path + File.separator + name
        val f = File(toFileName)
        if (f.exists()) return false
        val dir = File(path)
        if (!dir.exists()) dir.mkdirs()
        var `is`: InputStream? = null
        var fos: FileOutputStream? = null
        try {
            `is` = context.resources.openRawResource(
                context.resources.getIdentifier("initdb", "raw", context.packageName)
            )
            // 创建输出流
            fos = FileOutputStream(toFileName)
            // 将数据输出
            var buffer: ByteArray? = ByteArray(8192)
            var count = 0
            while (`is`.read(buffer).also { count = it } > 0) {
                fos.write(buffer, 0, count)
            }
            buffer = null
        } catch (ee: Exception) {
            FlowLog.log(FlowLog.Level.I, ee.toString())
        } finally {
            try {
                fos?.close()
            } catch (ee: Exception) {
            }
            try {
                `is`?.close()
            } catch (ee: Exception) {
            }
        }
        return true
    }

    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?
    ): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name!!), null)
    }

    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?,
        errorHandler: DatabaseErrorHandler?
    ): SQLiteDatabase {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name!!), null)
    }

}