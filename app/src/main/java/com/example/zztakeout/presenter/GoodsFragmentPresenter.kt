package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.bean.GoodsInfo
import com.example.zztakeout.model.bean.GoodsTypeInfo
import com.example.zztakeout.ui.activity.BusinessActivity
import com.example.zztakeout.ui.fragment.GoodsFragment
import com.example.zztakeout.utils.TakeoutApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class GoodsFragmentPresenter(val goodsFragment: GoodsFragment) :NetPresenter(){
    lateinit var goodsTypeInfo:ArrayList<GoodsTypeInfo>
    lateinit var allGoodsList:ArrayList<GoodsInfo>
    lateinit var cartGoodsList :  ArrayList<GoodsInfo>
    override fun parserJson(json: String) {
        Log.e("Takeout", " GoodsFragmentPresenter " + " parserJson")
        val jsonObject = JSONObject(json)
        val list = jsonObject.getString("list")
        val gson = Gson()
        goodsTypeInfo = gson.fromJson(list, object : TypeToken<List<GoodsTypeInfo>>() {}.type)
        val businessActivity = goodsFragment.activity as BusinessActivity

        allGoodsList = arrayListOf<GoodsInfo>()
        for (i in 0 until goodsTypeInfo.size) {
            val goodsTypeInfo = goodsTypeInfo.get(i)
            Log.e("Takeout", " GoodsFragmentPresenter " + " goodsTypeInfo" + goodsTypeInfo)
            var tvRedCount = 0
            if (businessActivity.hasSelectedGoods) {
                tvRedCount = TakeoutApplication.sInstance.queryCacheSelectedInfoByTypeId(goodsTypeInfo.id)
                goodsTypeInfo.tvRedDotCount = tvRedCount
            }
            val aTypeList: List<GoodsInfo> = goodsTypeInfo.list
            for (j in 0 until aTypeList.size) {
                val goodsInfo = aTypeList.get(j)
                goodsInfo.typeId = goodsTypeInfo.id
                goodsInfo.typeName = goodsTypeInfo.name
                if ( tvRedCount > 0 ){
                    Log.e("Takeout", " GoodsFragmentPresenter " + " goodsInfo.id = " + goodsInfo.id)
                    goodsInfo.count = TakeoutApplication.sInstance.queryCacheSelectedInfoByGoodsId(goodsInfo.id)
                }
                Log.e("Takeout", " GoodsFragmentPresenter " + " goodsInfo = " + goodsInfo)
            }
            allGoodsList.addAll(aTypeList)
            businessActivity.updateCartUi()
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
        cartGoodsList = ArrayList<GoodsInfo>()
        for (j in 0 until allGoodsList.size){
            if (allGoodsList.get(j).count > 0){
                cartGoodsList.add(allGoodsList.get(j))
            }
        }
        return cartGoodsList
    }

    fun clearCart() {
        for (j in 0 until allGoodsList.size) {
            if (allGoodsList.get(j).count != 0) {
                allGoodsList.get(j).count = 0
            }
        }
        for (i in 0 until goodsTypeInfo.size) {
            if (goodsTypeInfo.get(i).tvRedDotCount != 0) {
                goodsTypeInfo.get(i).tvRedDotCount = 0
            }
        }
        for (i in 0 until cartGoodsList.size) {
            if (cartGoodsList.get(i).count != 0) {
                cartGoodsList.get(i).count = 0
            }
        }
    }
}
