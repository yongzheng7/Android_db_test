package com.atom.testdb

import android.app.Application
import android.util.Log
import com.saop.core.SAOP

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SAOP.init(this)
        SAOP.debug(true)
        val packageName = applicationInfo.packageName
        Log.e("App" , packageName)
    }
}