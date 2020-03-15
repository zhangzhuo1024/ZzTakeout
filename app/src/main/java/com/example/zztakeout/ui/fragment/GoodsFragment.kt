package com.example.zztakeout.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.GoodsInfo
import com.example.zztakeout.model.bean.GoodsTypeInfo
import com.example.zztakeout.presenter.GoodsFragmentPresenter
import com.example.zztakeout.ui.adapter.GoodRvAdapter
import com.example.zztakeout.ui.adapter.GoodsLvAdapter
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/14
 */
class GoodsFragment : Fragment() {
    lateinit var rvGoods: RecyclerView
    lateinit var goodRvAdapter: GoodRvAdapter
    lateinit var goodsFragmentPresenter: GoodsFragmentPresenter

    lateinit var slhlv: StickyListHeadersListView
    lateinit var goodsLvAdapter: GoodsLvAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val goodsView = View.inflate(activity, R.layout.fragment_goods, null)
        rvGoods = goodsView.findViewById(R.id.rv_goods_type)
        rvGoods.layoutManager = LinearLayoutManager(activity)
        goodRvAdapter = GoodRvAdapter(this)
        rvGoods.adapter = goodRvAdapter


        slhlv = goodsView.findViewById<StickyListHeadersListView>(R.id.slhlv)
        goodsLvAdapter = GoodsLvAdapter()
        slhlv.adapter = goodsLvAdapter
        return goodsView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        goodsFragmentPresenter = GoodsFragmentPresenter(this)
        goodsFragmentPresenter.getBusinessInfo("1")
    }

    fun onGoodsSuccess(goodsTypeInfo: List<GoodsTypeInfo>, allGoodsList: ArrayList<GoodsInfo>) {
        goodRvAdapter.setData(goodsTypeInfo)
        goodsLvAdapter.setData(allGoodsList)
        slhlv.setOnScrollListener(object : AbsListView.OnScrollListener{
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

                val oldPosition = goodRvAdapter.selectPosition
                val typeId = goodsFragmentPresenter.allGoodsList.get(firstVisibleItem).typeId
                val newPositin = goodsFragmentPresenter.getTypePositinByTypeId(typeId)
                if (newPositin != oldPosition) {
                    goodRvAdapter.selectPosition = newPositin
                    goodRvAdapter.notifyDataSetChanged()
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

            }

        })
    }

    fun onGoodsFail() {
        Toast.makeText(activity, "服务器返回Goods数据失败", Toast.LENGTH_SHORT).show()
    }
}