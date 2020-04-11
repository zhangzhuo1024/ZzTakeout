package com.example.zztakeout.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.services.core.PoiItem
import com.example.zztakeout.R
import com.example.zztakeout.ui.activity.MapLocationActivity

class AroundRvAdapter(val mapLocationActivity: MapLocationActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mDatas: List<PoiItem> = ArrayList<PoiItem>()

    fun setData(data: List<PoiItem>) {
        mDatas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val goodsTypeItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_around_address, parent, false)
        return GoodsTypeItemHolder(goodsTypeItemView)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GoodsTypeItemHolder).bindData(mDatas[position])
    }

    inner class GoodsTypeItemHolder(var item: View) : RecyclerView.ViewHolder(item) {
        val tv_title: TextView
        val tv_address: TextView
        init {
            tv_title = item.findViewById(R.id.tv_title)
            tv_address = item.findViewById(R.id.tv_address)
            item.setOnClickListener {
                val intent = Intent()
                intent.putExtra("title",tv_title.text)
                intent.putExtra("address",tv_address.text)
                mapLocationActivity.setResult(200, intent)
                mapLocationActivity.finish()
            }
        }

        fun bindData(poiItem: PoiItem) {
            tv_title.text = poiItem.title
            tv_address.text = poiItem.snippet //摘要
        }
    }
}
