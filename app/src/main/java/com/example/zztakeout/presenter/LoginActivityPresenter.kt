package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.bean.User
import com.example.zztakeout.model.dao.TakeoutOpenHelper
import com.example.zztakeout.ui.activity.LoginActivity
import com.example.zztakeout.utils.TakeoutApplication
import com.google.gson.Gson
import com.j256.ormlite.android.AndroidDatabaseConnection
import com.j256.ormlite.dao.Dao
import java.sql.Savepoint

class LoginActivityPresenter(val loginActivity: LoginActivity) : NetPresenter() {
    fun loginByPhone(phone: String) {
        val loginByPhone = takeoutService.loginByPhone(phone)
        loginByPhone.enqueue(callback)

    }
    lateinit var connection :AndroidDatabaseConnection
    lateinit var savePoint :Savepoint
    override fun parserJson(json: String) {
        val user = Gson().fromJson<User>(json, User::class.java)
        if (user != null) {
            //缓存到内存中，登录界面结束后，可以快速显示用户信息
            TakeoutApplication.sUser = user
            //还要缓存到数据库之中，这样应用退出后下次进入时能保存用户登录状态
            //数据库使用SqliteOpenHelper，但是要自己写数据库语句，可以使用三方框架
            //常用ORM（对象关系映射）三方框架如：ormlite、greendao
            //ORM：对象-关系映射，实现JavaBean的属性到数据库表的字段的映射
            var takeoutOpenHelper = TakeoutOpenHelper(loginActivity)
            val dao: Dao<User, Int> = takeoutOpenHelper.getDao(User::class.java)
            //要区分新老用户，所以不能直接create
//            dao.create(user)

            try {
                connection = AndroidDatabaseConnection(takeoutOpenHelper.writableDatabase, true)
                savePoint = connection.setSavePoint("connent start")
                var isOldUser = false
                val queryForAll:List<User> = dao.queryForAll()
                for (u in queryForAll) {
                    if (user.id == u.id) isOldUser = true
                }
                if (isOldUser) {
                    dao.update(user)
                    Log.e("login", "老用户更新信息")
                } else {
                    dao.create(user)
                    Log.e("login", "新用户登录")
                }
                connection.commit(savePoint)
            } catch (e: Exception) {
                Log.e("Takeout", "出现ormlite事务异常," + e.localizedMessage)
                loginActivity.onLoginFail()
                connection.rollback(savePoint)
            }
            Log.e("Takeout", " LoginActivityPresenter " + "缓存本地信息到数据库成功 ")
            loginActivity.onLoginSuccess()
        } else {
            loginActivity.onLoginFail()
        }

    }
}
