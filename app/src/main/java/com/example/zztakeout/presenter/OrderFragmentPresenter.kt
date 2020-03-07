package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.bean.Order
import com.example.zztakeout.ui.fragment.OrderFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderFragmentPresenter(val orderFragment: OrderFragment) :NetPresenter(){
    override fun parserJson(json: String) {
        Log.e("Takeout", " OrderFragmentPresenter " + " parserJson")
        var orderList : List<Order> = Gson().fromJson(json, object : TypeToken<List<Order>>() {}.type)
        if (orderList.isEmpty()){
            orderFragment.onOrderFail()
        } else {
            orderFragment.onOrderSuccess(orderList)
        }
    }

    fun getOrderList(orderId: Int) {
        val orderList = takeoutService.getOrderList(orderId)
        orderList.enqueue(callback)
    }

}
