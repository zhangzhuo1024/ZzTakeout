package com.example.zztakeout.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.example.zztakeout.R
import com.example.zztakeout.presenter.LoginActivityPresenter
import com.example.zztakeout.smsSdk.privacy.OnDialogListener
import com.example.zztakeout.smsSdk.privacy.PrivacyDialog
import com.example.zztakeout.smsSdk.util.DemoSpHelper
import com.example.zztakeout.utils.SMSUtil
import com.mob.MobSDK
import com.mob.OperationCallback
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/4
 */
class LoginActivity : AppCompatActivity() {


    val eventHandler = object : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any) {
            if (data is Throwable) {
                Log.e("Takeout", " LoginActivity " + "EventHandler Throwable " + data.message)
            } else {
                when (event) {
                    SMSSDK.EVENT_GET_VERIFICATION_CODE ->
                        Log.e("Takeout", " LoginActivity " + "EventHandler 获取验证码成功 "
                    )
                    SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {
                        Log.e("Takeout", " LoginActivity " + "EventHandler 提交验证码成功 ")
                        val phone = etPhone.getText().toString().trim()
                        loginActivityPresenter.loginByPhone(phone)
                    }
                }
            }
        }
    }

    lateinit var loginActivityPresenter: LoginActivityPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginActivityPresenter = LoginActivityPresenter(this)
        initListener()
        SMSSDK.registerEventHandler(eventHandler)
    }

    override fun onDestroy() {
        super.onDestroy()
        SMSSDK.unregisterEventHandler(eventHandler)
    }


//    val eventHandler = Handler{
//        when(it.what){
//
//        }
//        false
//    }

    private fun initListener() {
        iv_user_back.setOnClickListener { finish() }
        tvCode.setOnClickListener {
            Log.e("Takeout", " LoginActivity " + "tvCode OnClick")
            checkPrivacyGranted()
        }
        tvVerify.setOnClickListener {
            val phone = etPhone.getText().toString().trim()
            val code = etCode.getText().toString().trim()
//            if (isNetworkConnected() && SMSUtil.judgePhoneNums(this, phone) && code.isNotEmpty()) {
//                SMSSDK.submitVerificationCode("86", phone, code)
//            }
            loginActivityPresenter.loginByPhone(phone)
        }
    }

    private fun checkPrivacyGranted() {
        Log.e(
            "Takeout",
            " LoginActivity " + " isPrivacyGranted  " + DemoSpHelper.getInstance().isPrivacyGranted()
        )
        if (!DemoSpHelper.getInstance().isPrivacyGranted()) {
            Log.e("Takeout", " LoginActivity " + "checkPrivacyGranted isPrivacyGranted")
            val privacyDialog = PrivacyDialog(this, object : OnDialogListener {
                override fun onAgree() {
                    Log.e("Takeout", " LoginActivity " + "PrivacyDialog onAgree")
                    uploadResult(true)
                    DemoSpHelper.getInstance().setPrivacyGranted(true)
                }

                override fun onDisagree() {
                    Log.e("Takeout", " LoginActivity " + "PrivacyDialog onDisagree")
                    uploadResult(false)
                    DemoSpHelper.getInstance().setPrivacyGranted(false)
                    val handler = Handler(Handler.Callback {
                        false
                    })
                    handler.sendEmptyMessageDelayed(0, 500)
                }
            })
            privacyDialog.show()
        } else {
            uploadResult(true)
        }
    }

    private fun uploadResult(granted: Boolean) {
        MobSDK.submitPolicyGrantResult(granted, object : OperationCallback<Void?>() {
            override fun onComplete(aVoid: Void?) {
                Log.e("Takeout", " LoginActivity " + "submitPolicyGrantResult onComplete")
                getTheTvCode()
            }

            override fun onFailure(throwable: Throwable) { // Nothing to do
                Log.e("Takeout", " LoginActivity " + "submitPolicyGrantResult onFailure")
            }
        })
    }

    private fun getTheTvCode() {
        if (!isNetworkConnected()) {
            Toast.makeText(this, getString(R.string.smssdk_network_error), Toast.LENGTH_SHORT)
                .show();
        }
        val phone = etPhone.getText().toString().trim()
        Log.e("Takeout", " LoginActivity " + "getTheTvCode" + phone)
        if (SMSUtil.judgePhoneNums(this, phone)) {
            SMSSDK.getVerificationCode("86", phone)
            tvCode.isEnabled = false
            Thread(CutDownTask()).start()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val manager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun onLoginSuccess() {
        Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun onLoginFail() {
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show()
    }

    companion object {
        val TIME_MINUS = -1
        val TIME_OUT = 0
    }

    val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                TIME_MINUS -> tvCode.text = "剩余时间${time}秒"
                TIME_OUT -> {
                    tvCode.isEnabled = true
                    tvCode.text = "点击重发"
                    time = 60
                }
            }
        }
    }

    //    val handler: Handler = Handler {
//        when (it.what) {
//            TIME_MINUS -> tvCode.text = "剩余事件${time}秒"
//            TIME_OUT -> tvCode.text = "点击重发"
//        }
//        false
//    }
    var time: Int = 60

    inner class CutDownTask : Runnable {

        override fun run() {
            while (time > 0) {
                handler.sendEmptyMessage(TIME_MINUS)
                SystemClock.sleep(999)
                time--
            }
            handler.sendEmptyMessage(TIME_OUT)
        }

    }

}


