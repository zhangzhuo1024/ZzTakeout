package com.example.zztakeout.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.GoodsInfo
import com.example.zztakeout.ui.activity.BusinessActivity
import com.example.zztakeout.utils.PriceFormater

class CartRvAdapter(businessActivity: BusinessActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mDatas: List<GoodsInfo> = ArrayList<GoodsInfo>()

    fun setData(data: ArrayList<GoodsInfo>) {
        mDatas = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cartItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartItemHolder(cartItemView)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("Takeout", " GoodRvAdapter  onBindViewHolder" + "mDatas = " + mDatas)
        (holder as CartItemHolder).bindData(mDatas[position])
    }

    inner class CartItemHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener {
        val tvGoodsName: TextView
        val tvAllPrice: TextView
        val tvCount: TextView
        val ibMinus: ImageButton
        val ibAdd: ImageButton
        lateinit var mGoodsInfo:GoodsInfo

        init {
            tvGoodsName = item.findViewById(R.id.tv_name)
            tvAllPrice = item.findViewById(R.id.tv_type_all_price)
            tvCount = item.findViewById(R.id.tv_count)
            ibMinus = item.findViewById(R.id.ib_minus)
            ibAdd = item.findViewById(R.id.ib_add)
            ibMinus.setOnClickListener(this)
            ibAdd.setOnClickListener(this)
        }

        fun bindData(goodsType: GoodsInfo) {
            mGoodsInfo = goodsType
            tvGoodsName.text = goodsType.name
            tvCount.text = goodsType.count.toString()
            tvAllPrice.text = PriceFormater.format(goodsType.count * goodsType.newPrice.toFloat())
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.ib_add -> {
                    Log.e("1111111111", "ib_add = ")
                    doAddOperation()
                }

                R.id.ib_minus -> doMinusOperation()
            }

        }

        private fun doMinusOperation() {
            var count = mGoodsInfo.count
            count--
            mGoodsInfo.count = count
            Log.e("1111111111", "dddddddddddd = " + count)
            notifyDataSetChanged()

        }

        private fun doAddOperation() {
            mGoodsInfo.count++
            notifyDataSetChanged()

        }
    }
}
