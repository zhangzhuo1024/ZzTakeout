package com.example.zztakeout.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.Order
import com.example.zztakeout.utils.OrderObservable

class OrderRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mDatas: List<Order> = ArrayList<Order>()

    fun setData(data: List<Order>) {
        mDatas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val orderItemView = LayoutInflater.from(context).inflate(R.layout.item_order_item, parent, false)
        return OrderItemHolder(orderItemView)

//        return OrderItemHolder(View.inflate(context, R.layout.item_order_item, null))

    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrderItemHolder).bindData(mDatas[position])
    }

    inner class OrderItemHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvOrderName: TextView
        val tvOrderType: TextView
        lateinit var mOrder: Order

        init {
            tvOrderName = item.findViewById(R.id.tv_order_item_seller_name)
            tvOrderType = item.findViewById(R.id.tv_order_item_type) //订单状态
        }

        fun bindData(order: Order) {
            this.mOrder = order

            tvOrderName.text = order.seller.name
            tvOrderType.text = getOrderTypeInfo(order.type)

        }
    }

    private fun getOrderTypeInfo(type: String): String {
        /**
         * 订单状态
         * 1 未支付 2 已提交订单 3 商家接单  4 配送中,等待送达 5已送达 6 取消的订单
         */
        //            public static final String ORDERTYPE_UNPAYMENT = "10";
        //            public static final String ORDERTYPE_SUBMIT = "20";
        //            public static final String ORDERTYPE_RECEIVEORDER = "30";
        //            public static final String ORDERTYPE_DISTRIBUTION = "40";
        //            public static final String ORDERTYPE_SERVED = "50";
        //            public static final String ORDERTYPE_CANCELLEDORDER = "60";

        var typeInfo = ""
        when (type) {
            OrderObservable.ORDERTYPE_UNPAYMENT -> typeInfo = "未支付"
            OrderObservable.ORDERTYPE_SUBMIT -> typeInfo = "已提交订单"
            OrderObservable.ORDERTYPE_RECEIVEORDER -> typeInfo = "商家接单"
            OrderObservable.ORDERTYPE_DISTRIBUTION -> typeInfo = "配送中"
            OrderObservable.ORDERTYPE_SERVED -> typeInfo = "已送达"
            OrderObservable.ORDERTYPE_CANCELLEDORDER -> typeInfo = "取消的订单"
        }
        return typeInfo
    }

}
