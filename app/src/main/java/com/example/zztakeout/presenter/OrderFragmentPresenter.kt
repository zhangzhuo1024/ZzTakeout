package com.example.zztakeout.presenter

import android.util.Log
import com.example.zztakeout.model.bean.Order
import com.example.zztakeout.model.net.ResponseInfo
import com.example.zztakeout.ui.fragment.OrderFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


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
        //直接使用Retrofit进行异步加载
//        val orderList = takeoutService.getOrderList(orderId)
//        orderList.enqueue(callback)

        //使用Retrofit+RXjava实现异步加载
        val observable = takeoutService.getOrderListByRxjava(orderId)
//        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<ResponseInfo> {
//                    override fun onCompleted() {
//                    }
//
//                    override fun onError(e: Throwable?) {
//                        if (e != null) {
//                            Log.e("rxjava", e.localizedMessage);
//                        }
//                    }
//
//                    override fun onNext(responseInfo: ResponseInfo?) {
//                        if (responseInfo != null) {
//                            parserJson(responseInfo.data)
//                        }
//                    }
//                })
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                parserJson(it.data)
            }, {
                if (it != null) {
                    Log.e("rxjava", it.localizedMessage);
                }
            }, {
                Log.e("rxjava", "onComplete!");
            })
    }

}
