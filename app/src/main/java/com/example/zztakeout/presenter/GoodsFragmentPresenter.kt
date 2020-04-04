package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.bean.GoodsInfo
import com.example.zztakeout.model.bean.GoodsTypeInfo
import com.example.zztakeout.ui.fragment.GoodsFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class GoodsFragmentPresenter(val goodsFragment: GoodsFragment) :NetPresenter(){
    lateinit var goodsTypeInfo:ArrayList<GoodsTypeInfo>
    lateinit var allGoodsList:ArrayList<GoodsInfo>
    override fun parserJson(json: String) {
        Log.e("Takeout", " GoodsFragmentPresenter " + " parserJson")
        val jsonObject = JSONObject(json)
        val list = jsonObject.getString("list")
        val gson = Gson()
        goodsTypeInfo = gson.fromJson(list, object : TypeToken<List<GoodsTypeInfo>>() {}.type)
        Log.e("Takeout", " GoodsFragmentPresenter " + " goodsTypeInfo" + goodsTypeInfo)

        allGoodsList = arrayListOf<GoodsInfo>()
        for (i in 0 until goodsTypeInfo.size) {
            val goodsTypeInfo = goodsTypeInfo.get(i)
            val aTypeList: List<GoodsInfo> = goodsTypeInfo.list
            aTypeList.forEach {
                it.typeId = goodsTypeInfo.id
                it.typeName = goodsTypeInfo.name
            }
            allGoodsList.addAll(aTypeList)
        }


        //获取到数据，刷新界面
        if (goodsTypeInfo.isNotEmpty()) {
            Log.e("Takeout", " GoodsFragmentPresenter " + "goodsTypeInfo = " + goodsTypeInfo)
            goodsFragment.onGoodsSuccess(goodsTypeInfo, allGoodsList)
        } else {
            goodsFragment.onGoodsFail()
        }
    }

    fun getBusinessInfo(sellerId: String) {
        val goodsInfo = takeoutService.getBusinessInfo(sellerId)
        goodsInfo.enqueue(callback)
    }

    fun getGoodsPositionByTypeId(typeId: Int): Int {
        var firstPosition: Int = -1
        for (j in 0 until allGoodsList.size) {
            Log.e("Takeout", " GoodsFragmentPresenter " + " getGoodsPositionByTypeId" + allGoodsList.get(j).typeId)
            if (allGoodsList.get(j).typeId == typeId) {
                firstPosition = j
                break
            }
        }
        return firstPosition
    }

    fun getTypePositinByTypeId(typeId: Int): Int {
        var typePosition: Int = -1

        for (i in 0 until goodsTypeInfo.size) {
            if (goodsTypeInfo.get(i).id == typeId) {
                typePosition = i
                break
            }
        }
        return typePosition
    }

    fun getCartList() : ArrayList<GoodsInfo> {
        var cartGoodsList = ArrayList<GoodsInfo>()
        for (j in 0 until allGoodsList.size){
            if (allGoodsList.get(j).count > 0){
                cartGoodsList.add(allGoodsList.get(j))
            }
        }
        return cartGoodsList
    }

}
