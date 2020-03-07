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

class HomeFragmentPresenter(var homeFragment: HomeFragment) : NetPresenter() {


    fun getHomeInfo() {
        val homeInfo = takeoutService.getHomeInfo()
        homeInfo.enqueue(callback)
    }

    override fun parserJson(json: String) {
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