package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.bean.Seller
import com.example.zztakeout.model.net.ResponseInfo
import com.example.zztakeout.model.net.TakeoutService
import com.example.zztakeout.ui.fragment.HomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragmentPresenter(var homeFragment: HomeFragment) {
    var HOST = "http://10.0.245.30:8080"
    var takeoutService: TakeoutService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(HOST + "/TakeoutService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        takeoutService = retrofit.create<TakeoutService>(TakeoutService::class.java!!)
    }

    fun getHomeInfo() {
        val homeInfo = takeoutService.getHomeInfo()
        homeInfo.enqueue(object : Callback<ResponseInfo> {
            override fun onFailure(call: Call<ResponseInfo>?, t: Throwable?) {
                Log.e("Takeout", " HomeFragmentPresenter " + "getHomeInfo onFailure")
                Log.e("Takeout", " HomeFragmentPresenter " + call)
                Log.e("Takeout", " HomeFragmentPresenter " + t)

            }

            override fun onResponse(call: Call<ResponseInfo>?, response: Response<ResponseInfo>?) {
                Log.e("Takeout", " HomeFragmentPresenter " + "getHomeInfo onResponse")
                if (response == null) {
                    Log.e("Takeout", " HomeFragmentPresenter " + "服务器没有成功返回")
                } else {
                    if (response.isSuccessful) {
                        val responseInfo = response.body()
                        if (responseInfo.code.equals("0")) {
                            val json = responseInfo.data
                            parserJson(json)
                        }
                    }else{
                        Log.e("Takeout", " HomeFragmentPresenter " + "服务器代码错误")
                    }
                }
            }

        })
    }

    private fun parserJson(json: String) {
        val jsonObject = JSONObject(json)
        val nearby = jsonObject.getString("nearbySellerList")
        val other = jsonObject.getString("otherSellerList")
        Log.e("home", nearby)
        val gson = Gson()
        val nearbySellers: List<Seller> = gson.fromJson(nearby, object : TypeToken<List<Seller>>() {}.type)
        val otherSellers: List<Seller> = gson.fromJson(other, object : TypeToken<List<Seller>>() {}.type)

        //获取到数据，刷新界面
        if (nearby.isNotEmpty() || otherSellers.isNotEmpty()) {
            homeFragment.onHomeSuccess(nearbySellers, otherSellers)
        } else {
            homeFragment.onHomeFail()
        }
    }

}