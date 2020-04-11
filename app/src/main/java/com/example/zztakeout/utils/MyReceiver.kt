package com.example.zztakeout.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import cn.jpush.android.api.JPushInterface
import org.json.JSONObject


class MyReceiver: BroadcastReceiver() {
    private val TAG = "Takeout"

    private var nm: NotificationManager? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("Takeout", " MyReceiver " + " 接收到推送")
        if (context == null || intent == null) {
            Log.e("Takeout", " MyReceiver " + " context == null || intent == null")
            return
        }
        if (null == nm) {
            nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        val bundle = intent.extras
        if (bundle == null) return

        if (JPushInterface.ACTION_REGISTRATION_ID == intent.action) {
            Log.e(TAG, "JPush 用户注册成功")
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action) {
            Log.e(TAG, "接受到推送下来的自定义消息")
            val message:String? = bundle.getString(JPushInterface.EXTRA_MESSAGE)
//            Log.e("order", message)  //消息内容
            val extras:String? = bundle.getString(JPushInterface.EXTRA_EXTRA)
            if(!TextUtils.isEmpty(extras)){
                Log.e(TAG, "extras : $extras")  //附加字段
                //方法一：通过mainactivity ---> orderFragment ---> OrderRvAdapter ---> 第二个条目的tvOrderType ---> 改变值40
                //方法二：使用观察者模式(OrderRvAdapter是观察者，MyReceiver是被观察者）
                OrderObservable.instance.newMsgComing(extras!!)

            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action) {
            Log.e(TAG, "接受到推送下来的通知")
            receivingNotification(context, bundle)
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action) {
            Log.e(TAG, "用户点击打开了通知")
            openNotification(context, bundle)
        } else {
            Log.e(TAG, "Unhandled intent - " + intent.action)
        }
    }

    private fun receivingNotification(context: Context, bundle: Bundle) {
        val title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE)
        Log.d(TAG, " title : $title")
        val message = bundle.getString(JPushInterface.EXTRA_ALERT)
        Log.d(TAG, "message : $message")
        val extras = bundle.getString(JPushInterface.EXTRA_EXTRA)
        Log.d(TAG, "extras : $extras")
    }

    private fun openNotification(
        context: Context,
        bundle: Bundle
    ) {
        val extras = bundle.getString(JPushInterface.EXTRA_EXTRA)
        var myValue = ""
        myValue = try {
            val extrasJson = JSONObject(extras)
            extrasJson.optString("myKey")
        } catch (e: Exception) {
            Log.w(TAG, "Unexpected: extras is not a valid json", e)
            return
        }
//        if (TYPE_THIS.equals(myValue)) {
//            val mIntent = Intent(context, ThisActivity::class.java)
//            mIntent.putExtras(bundle)
//            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(mIntent)
//        } else if (TYPE_ANOTHER.equals(myValue)) {
//            val mIntent = Intent(context, AnotherActivity::class.java)
//            mIntent.putExtras(bundle)
//            mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(mIntent)
//        }
    }
}