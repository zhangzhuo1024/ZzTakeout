package com.example.zztakeout.ui.fragment

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.zztakeout.R
import com.example.zztakeout.model.bean.Order
import com.example.zztakeout.presenter.OrderFragmentPresenter
import com.example.zztakeout.ui.adapter.OrderRvAdapter
import com.example.zztakeout.utils.TakeoutApplication

class OrderFragment : Fragment() {
    lateinit var rvOrder: RecyclerView
    lateinit var orderRvAdapter: OrderRvAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val orderView = inflater.inflate(R.layout.fragment_order, container, false)
        rvOrder = orderView.findViewById<RecyclerView>(R.id.rv_order_list)
        rvOrder.layoutManager = LinearLayoutManager(activity)
        orderRvAdapter = OrderRvAdapter(activity)
        rvOrder.adapter = orderRvAdapter
        return orderView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (TakeoutApplication.sUser.id == -1) {
            Toast.makeText(activity, "请先登录才能查看订单", Toast.LENGTH_SHORT).show()
        } else {
            initData()
        }
    }

    private fun initData() {

        var orderFragmentPresenter = OrderFragmentPresenter(this)
        orderFragmentPresenter.getOrderList(TakeoutApplication.sUser.id)
    }

    fun onOrderSuccess(orderList: List<Order>) {
        orderRvAdapter.setData(orderList)
    }

    fun onOrderFail() {
        Toast.makeText(activity, "服务器返回数据失败", Toast.LENGTH_SHORT).show()
    }
}
