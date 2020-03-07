package com.example.zztakeout.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.Order

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

        }
    }

}
