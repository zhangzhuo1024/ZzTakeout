package com.example.zztakeout.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.GoodsTypeInfo
import com.example.zztakeout.ui.fragment.GoodsFragment

class GoodRvAdapter(val goodsFragment : GoodsFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mDatas: List<GoodsTypeInfo> = ArrayList<GoodsTypeInfo>()

    fun setData(data: List<GoodsTypeInfo>) {
        Log.e("Takeout", " GoodRvAdapter " + "data = " + data)
        mDatas = data
        Log.e("Takeout", " GoodRvAdapter " + "mDatas = " + mDatas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.e("Takeout", " GoodRvAdapter  onCreateViewHolder")
        val goodsTypeItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false)
        return GoodsTypeItemHolder(goodsTypeItemView)
    }

    override fun getItemCount(): Int {
        Log.e("Takeout", " GoodRvAdapter  getItemCount" + "mDatas.size = " + mDatas.size)
        return mDatas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("Takeout", " GoodRvAdapter  onBindViewHolder" + "mDatas = " + mDatas)
        (holder as GoodsTypeItemHolder).bindData(mDatas[position], position)
    }

    var selectPosition: Int = 0
    inner class GoodsTypeItemHolder(var item: View) : RecyclerView.ViewHolder(item) {
        val tvGoodsTypeName: TextView
        val tvRedDotCount: TextView
        var mPosition: Int = 0
        lateinit var mGoodsType :GoodsTypeInfo

        init {
            Log.e("Takeout", " GoodRvAdapter " + "init = ")
            tvGoodsTypeName = item.findViewById(R.id.type)
            tvRedDotCount = item.findViewById(R.id.tvRedDotCount)
            item.setOnClickListener {
                selectPosition = mPosition
                mDatas.get(mPosition).id
                val goodsPositionByTypeId = goodsFragment.goodsFragmentPresenter.getGoodsPositionByTypeId(mGoodsType.id)
                goodsFragment.slhlv.setSelection(goodsPositionByTypeId)
                notifyDataSetChanged()
            }
        }

        fun bindData(goodsType: GoodsTypeInfo, position: Int) {
            mGoodsType = goodsType
            if (position == selectPosition) {
                //选中的为白底加粗黑字
                item.setBackgroundColor(Color.WHITE)
                tvGoodsTypeName.setTextColor(Color.BLACK)
                tvGoodsTypeName.setTypeface(Typeface.DEFAULT_BOLD)
            } else {
                //未选中是灰色背景 普通字体
                item.setBackgroundColor(Color.parseColor("#b9dedcdc"))
                tvGoodsTypeName.setTextColor(Color.GRAY)
                tvGoodsTypeName.setTypeface(Typeface.DEFAULT)
            }
            tvGoodsTypeName.text = goodsType.name
            tvRedDotCount.text = goodsType.tvRedDotCount.toString()
            Log.e("Takeout", " GoodRvAdapter " + "tvRedDotCount.text  = " + tvRedDotCount.text )

            if (goodsType.tvRedDotCount > 0) {
                tvRedDotCount.visibility = View.VISIBLE
            } else {
                tvRedDotCount.visibility = View.GONE
            }
            mPosition = position
        }
    }
}
