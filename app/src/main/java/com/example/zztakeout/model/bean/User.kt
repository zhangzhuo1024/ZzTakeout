package com.example.zztakeout.model.bean

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "t_user")
class User {
    @DatabaseField(id = true)
    var id: Int = 0
    @DatabaseField(columnName = "name")
    var name: String? = null
    @DatabaseField(columnName = "balance")
    var balance = 0f
    @DatabaseField(columnName = "discount")
    var discount = 0
    @DatabaseField(columnName = "integral")
    var integral = 0
    @DatabaseField(columnName = "phone")
    var phone: String? = null

}