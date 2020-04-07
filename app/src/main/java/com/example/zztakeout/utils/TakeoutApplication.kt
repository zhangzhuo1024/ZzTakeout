package com.example.zztakeout.utils

import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.example.zztakeout.model.bean.CacheSelectedInfo
import com.example.zztakeout.model.bean.User
import com.mob.MobApplication
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.properties.Delegates

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/6
 */
class TakeoutApplication : MobApplication() {

    companion object {
        var sUser: User = User()
        var sInstance : TakeoutApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("Takeout", " TakeoutApplication onCreate")
        sUser.id = -1
        sInstance = this
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }


    //点餐缓存集合使用的是CopyOnWriteArrayList，这是一个线程安全的集合，添加和移除的时候都是加锁，查看源码
    var selectedList: CopyOnWriteArrayList<CacheSelectedInfo> = CopyOnWriteArrayList<CacheSelectedInfo>()

    fun queryCacheSelectedInfoByGoodsId(goodsId: Int): Int {
        var count = 0
//        通过java转化的代码
//        for (i in 0..selectedList.size - 1) {
//            val (_, _, goodsId1, count1) = selectedList[i]
//            if (goodsId1 == goodsId) {
//                count = count1
//                break
//            }
//        }
        for (i in 0 until selectedList.size) {
            val cacheSelectedInfo = selectedList[i]
            if (cacheSelectedInfo.goodsId == goodsId) {
                count = cacheSelectedInfo.count
            }
        }
        return count
    }

    fun queryCacheSelectedInfoByTypeId(typeId: Int): Int {
        var count = 0
        for (i in 0..selectedList.size - 1) {
            val (_, typeId1, _, count1) = selectedList[i]
            if (typeId1 == typeId) {
                count = count + count1
            }
        }
        return count
    }

    fun queryCacheSelectedInfoBySellerId(sellerId: Int): Int {
        var count = 0
        for (i in 0..selectedList.size - 1) {
            val (sellerId1, _, _, count1) = selectedList[i]
            if (sellerId1 == sellerId) {
                count = count + count1
            }
        }
        return count
    }

    fun addCacheSelectedInfo(info: CacheSelectedInfo) {
        selectedList.add(info)
    }

    fun clearCacheSelectedInfo(sellerId: Int) {
        val temp = ArrayList<CacheSelectedInfo>()
        for (i in 0..selectedList.size - 1) {
            val info = selectedList[i]
            if (info.sellerId == sellerId) {
//                selectedList.remove(info)
                temp.add(info)
            }
        }
        selectedList.removeAll(temp)
    }

    fun deleteCacheSelectedInfo(goodsId: Int) {
        for (i in 0..selectedList.size - 1) {
            val info = selectedList[i]
            if (info.goodsId == goodsId) {
                selectedList.remove(info)
                break
            }
        }
    }

    fun updateCacheSelectedInfo(goodsId: Int, operation: Int) {
        for (i in 0..selectedList.size - 1) {
            var info = selectedList[i]
            if (info.goodsId == goodsId) {
                when (operation) {
                    Constants.ADD -> info.count = info.count + 1
                    Constants.MINUS -> info.count = info.count - 1
                }

            }
        }
    }
}