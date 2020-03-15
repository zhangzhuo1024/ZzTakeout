package com.example.zztakeout.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.GoodsInfo
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter
import java.util.ArrayList

class GoodsLvAdapter : BaseAdapter(), StickyListHeadersAdapter {
    var mDatas: List<GoodsInfo> = ArrayList<GoodsInfo>()

    fun setData(allGoodsList: ArrayList<GoodsInfo>) {
        mDatas = allGoodsList
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
        var itemView : View
        var goodsItemHolder:GoodsItemHolder
        if (convertView == null) {
            itemView =
                LayoutInflater.from(parent?.context).inflate(R.layout.item_goods, parent, false)
            goodsItemHolder = GoodsItemHolder(itemView)
            itemView.tag = goodsItemHolder
        } else {
            itemView = convertView
            goodsItemHolder = itemView.tag as GoodsItemHolder
        }
        goodsItemHolder.bindData(mDatas.get(position))
        return itemView
    }

    inner class GoodsItemHolder(itemView: View) : View.OnClickListener {
        val ivIcon: ImageView
        val tvName: TextView
        val tvForm: TextView
        val tvMonthSale: TextView
        val tvNewPrice: TextView
        val tvOldPrice: TextView
        val btnAdd: ImageButton
        val btnMinus: ImageButton
        val tvCount: TextView

        init {
            ivIcon = itemView.findViewById(R.id.iv_icon)
            tvName = itemView.findViewById(R.id.tv_name)
            tvForm = itemView.findViewById(R.id.tv_form)
            tvMonthSale = itemView.findViewById(R.id.tv_month_sale)
            tvNewPrice = itemView.findViewById(R.id.tv_newprice)
            tvOldPrice = itemView.findViewById(R.id.tv_oldprice)
            tvCount = itemView.findViewById(R.id.tv_count)
            btnAdd = itemView.findViewById(R.id.ib_add)
            btnMinus = itemView.findViewById(R.id.ib_minus)
            btnAdd.setOnClickListener(this)
            btnMinus.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

        }

        fun bindData(goodsInfo: GoodsInfo) {
            tvName.text = goodsInfo.name
        }
    }

    override fun getItem(position: Int): Any {
        return mDatas.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getHeaderId(position: Int): Long {
        val goodsInfo = mDatas.get(position)
        return goodsInfo.typeId.toLong()
    }

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val goodsInfo = mDatas.get(position)
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_type_header, parent, false) as TextView
        itemView.text = goodsInfo.typeName
        return itemView
    }
}
