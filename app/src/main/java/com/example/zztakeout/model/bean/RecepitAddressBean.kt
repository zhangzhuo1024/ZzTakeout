package com.example.zztakeout.model.bean

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.io.Serializable

@DatabaseTable(tableName = "t_address")
class RecepitAddressBean : Serializable{
    @DatabaseField(generatedId = true)
    var id: Int = 0
    @DatabaseField(columnName = "username")
    var username: String = ""
    @DatabaseField(columnName = "sex")
    var sex: String = "女"
    @DatabaseField(columnName = "phone")
    var phone: String = ""
    @DatabaseField(columnName = "address")
    var address: String = ""
    @DatabaseField(columnName = "detailAddress")
    var detailAddress: String = ""
    @DatabaseField(columnName = "label")
    var label: String = ""
    @DatabaseField(columnName = "userId")
    var userId: String = "38"

    constructor()

    constructor(id: Int, username: String, sex: String, phone: String, address: String, detailAddress: String,label: String, userId: String) {
        this.id = id
        this.username = username
        this.sex = sex
        this.phone = phone
        this.address = address
        this.detailAddress = detailAddress
        this.label = label
        this.userId = userId
    }
}