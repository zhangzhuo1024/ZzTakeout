package com.example.zztakeout.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.smssdk.SMSSDK
import com.example.zztakeout.R
import com.example.zztakeout.smsSdk.privacy.OnDialogListener
import com.example.zztakeout.smsSdk.privacy.PrivacyDialog
import com.example.zztakeout.smsSdk.util.DemoSpHelper
import com.mob.MobSDK
import com.mob.OperationCallback
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/4
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initListener()
    }

    private fun initListener() {
        iv_user_back.setOnClickListener { finish() }
        tvCode.setOnClickListener {
            Log.e("Takeout", " LoginActivity " + "tvCode OnClick")
            checkPrivacyGranted()
        }
        tvVerify.setOnClickListener {

        }
    }

    private fun checkPrivacyGranted() {
        Log.e("Takeout", " LoginActivity " + " isPrivacyGranted  " +DemoSpHelper.getInstance().isPrivacyGranted())
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
        Log.e("Takeout", " LoginActivity " + "getTheTvCode" + etPhone.getText().toString().trim())
        SMSSDK.getVerificationCode("86", etPhone.getText().toString().trim());
    }

    private fun isNetworkConnected(): Boolean {
        val manager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return if (networkInfo != null && networkInfo.isConnected) {
            true
        } else false
    }

}