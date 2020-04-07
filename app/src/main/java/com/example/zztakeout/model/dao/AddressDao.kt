package com.example.zztakeout.model.dao

import android.content.Context
import android.util.Log
import com.example.zztakeout.model.bean.RecepitAddressBean
import com.j256.ormlite.dao.Dao

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/4/7
 */
class AddressDao(context: Context) {
    lateinit private var addressDao: Dao<RecepitAddressBean, Int>

    init {
        var takeoutOpenHelper = TakeoutOpenHelper(context)
        addressDao = takeoutOpenHelper.getDao(RecepitAddressBean::class.java)
    }

    fun addAddressBean(addressBean: RecepitAddressBean) {
        try {
            addressDao.create(addressBean)
        } catch (e: Exception) {
            Log.e("Takeout", " AddressDao add " + e)
        }
    }

    fun deleteAddressBean(addressBean: RecepitAddressBean) {
        try {
            addressDao.delete(addressBean)
        } catch (e: Exception) {
            Log.e("Takeout", " AddressDao add " + e)
        }
    }

    fun updateAddressBean(addressBean: RecepitAddressBean) {
        try {
            addressDao.update(addressBean)
        } catch (e: Exception) {
            Log.e("Takeout", " AddressDao add " + e)
        }
    }

    fun queryAddressBean(): List<RecepitAddressBean> {
        try {
            return addressDao.queryForAll()
        } catch (e: Exception) {
            return ArrayList<RecepitAddressBean>()
            Log.e("Takeout", " AddressDao add " + e)
        }
    }
}