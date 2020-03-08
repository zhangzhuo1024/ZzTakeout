package com.example.zztakeout.utils

import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.example.zztakeout.model.bean.User
import com.mob.MobApplication

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/6
 */
class TakeoutApplication : MobApplication() {

    companion object {
        var sUser: User = User()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("Takeout", " TakeoutApplication onCreate")
        sUser.id = -1
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}