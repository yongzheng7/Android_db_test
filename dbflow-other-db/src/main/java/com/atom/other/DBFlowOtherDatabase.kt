package com.atom.other

import com.raizlabs.android.dbflow.annotation.Database

@Database(name = DBFlowOtherDatabase.NAME, version = DBFlowOtherDatabase.VERSION)
class DBFlowOtherDatabase {
    companion object{
        const val NAME = "DBFlowOtherDatabase"
        const val VERSION = 2
    }
}