package com.atom.other

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = DBFlowOtherDatabase::class)
class UserOther : BaseModel() {
    @Column
    @PrimaryKey
    private var id: String? = null

    @Column(length = 32)
    private var name: String? = null


    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }


    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    override fun toString(): String {
        return "UserOther(id=$id, name=$name)"
    }
}