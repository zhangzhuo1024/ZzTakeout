package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.net.ResponseInfo
import com.example.zztakeout.model.net.TakeoutService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/5
 */
abstract class NetPresenter {
    var HOST = "http://192.168.0.7:8080"
    var takeoutService: TakeoutService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(HOST + "/TakeoutService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        takeoutService = retrofit.create<TakeoutService>(TakeoutService::class.java!!)
    }

    var callback: Callback<ResponseInfo> = object : Callback<ResponseInfo> {
        override fun onFailure(call: Call<ResponseInfo>?, t: Throwable?) {
            Log.e("Takeout", " NetPresenter " + "getInfo onFailure")
        }

        override fun onResponse(call: Call<ResponseInfo>?, response: Response<ResponseInfo>?) {
            Log.e("Takeout", " NetPresenter " + " onResponse")
            if (response == null) {
                Log.e("Takeout", " NetPresenter " + "服务器没有成功返回")
            } else {
                if (response.isSuccessful) {
                    val responseInfo = response.body()
                    if (responseInfo.code.equals("0")) {
                        val json = responseInfo.data
                        parserJson(json)
                    }
                }else{
                    Log.e("Takeout", " NetPresenter " + "服务器代码错误")
                }
            }
        }

    }

    abstract fun parserJson(json: String)

}