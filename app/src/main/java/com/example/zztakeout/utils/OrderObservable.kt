package com.example.zztakeout.utils

import java.util.*

/**
 * 订单状态被观察者
 */
class OrderObservable private constructor() : Observable() {
    fun newMsgComing(extras: String) {
        //从广播接受者获取到最新消息
        //通知所有观察者，新消息来了
        setChanged()
        notifyObservers(extras)
    }

    companion object {
        val instance = OrderObservable()

        /* 订单状态
       * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送达 5已送达 6 取消的订单*/
        const val ORDERTYPE_UNPAYMENT = "10"
        const val ORDERTYPE_SUBMIT = "20"
        const val ORDERTYPE_RECEIVEORDER = "30"
        const val ORDERTYPE_DISTRIBUTION = "40"
        // 骑手状态：接单、取餐、送餐
        const val ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE = "43"
        const val ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL = "46"
        const val ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL = "48"
        const val ORDERTYPE_SERVED = "50"
        const val ORDERTYPE_CANCELLEDORDER = "60"
    }
}