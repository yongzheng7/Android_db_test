package com.atom.dbflow

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = DBFlowDatabase.NAME, version = DBFlowDatabase.VERSION)
class DBFlowDatabase {
    companion object{
        const val NAME = "DBFlowDatabase"
        const val VERSION = 2
    }
}