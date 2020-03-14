package com.example.zztakeout.utils

import java.util.*

/**
 * 订单状态被观察者
 * 观察者模式：通过极光推送的订单更新的消息会在MyReceiver中收到，收到之后需要刷新ui
 * 方法一：通过mainactivity ---> orderFragment ---> OrderRvAdapter ---> 第二个条目的tvOrderType ---> 改变值40
 * 方法二：使用观察者模式
 * 使用观察者模式三要素：被观察者、观察者、数据流
 * 被观察者：OrderObservable类，实现Observable接口成为被观察者
 * 观察者：最终目的O是在OrderRvAdapter中更新ui，所以观察者是OrderRvAdapter，它需要实现接口Observer，
 * 并绑定被观察者OrderObservable类，还要重写回调方法用于获取观察到的更新数据
 * 数据流：极光推送的数据被推送到MyReceiver的onReceive中，在然后将数据给被观察者OrderObservable类，
 * 被观察者收到数据后刷新数据，观察者OrderRvAdapter中的update就会接收到数据，然后完成刷新
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