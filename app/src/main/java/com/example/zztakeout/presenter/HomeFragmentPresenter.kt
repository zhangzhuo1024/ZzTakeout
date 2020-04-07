package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.bean.Seller
import com.example.zztakeout.ui.fragment.HomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class HomeFragmentPresenter(var homeFragment: HomeFragment) : NetPresenter() {


    fun getHomeInfo() {
        val homeInfo = takeoutService.getHomeInfo()
        homeInfo.enqueue(callback)
    }

    override fun parserJson(json: String) {
        val jsonObject = JSONObject(json)
        val nearby = jsonObject.getString("nearbySellerList")
        val other = jsonObject.getString("otherSellerList")
        Log.e("Takeout", " HomeFragmentPresenter " + " parserJson")
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