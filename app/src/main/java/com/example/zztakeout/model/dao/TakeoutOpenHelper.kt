package com.example.zztakeout.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.zztakeout.model.bean.RecepitAddressBean
import com.example.zztakeout.model.bean.User
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/7
 */
//class TakeoutOpenHelper
//    (
//    context: Context,
//    databaseName: String,
//    factory: SQLiteDatabase.CursorFactory,
//    databaseVersion: Int
//) : OrmLiteSqliteOpenHelper(context, databaseName, factory, databaseVersion) {

//新建TakeoutOpenHelper继承OrmLiteSqliteOpenHelper，在构造函数中直接建立数据库takeout_kotlin_db
class TakeoutOpenHelper(context: Context) : OrmLiteSqliteOpenHelper(context, "takeout_kotlin_db", null, 2) {
    override fun onCreate(database: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        //在onCreate中创建数据库中的表
        TableUtils.createTable(connectionSource, User::class.java)

        //创建地址的表
        TableUtils.createTable(connectionSource, RecepitAddressBean::class.java)
    }

    override fun onUpgrade(database: SQLiteDatabase?, connectionSource: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        //版本升级时处理
        if (newVersion == 2) {
            //升级用户已经有原来的表，只要在这里添加新增表
            TableUtils.createTable(connectionSource, RecepitAddressBean::class.java)
        }
    }


}