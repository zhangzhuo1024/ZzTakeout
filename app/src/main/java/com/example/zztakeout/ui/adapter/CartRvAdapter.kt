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
import com.example.zztakeout.ui.fragment.GoodsFragment
import com.example.zztakeout.utils.PriceFormater

class CartRvAdapter(val businessActivity: BusinessActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mDatas: ArrayList<GoodsInfo> = ArrayList<GoodsInfo>()

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
            if (count == 1){
                mDatas.remove(mGoodsInfo)
            }
            if (mDatas.size < 1) {
                businessActivity.showOrHideCart()
            }
            count--
            mGoodsInfo.count = count
            notifyDataSetChanged()
            updateRedCountTv(false)
            businessActivity.updateCartUi()
        }

        private fun updateRedCountTv(isAdd: Boolean) {
            val goodsFragment = businessActivity.fragmentList[0] as GoodsFragment
            val typePositin = goodsFragment.goodsFragmentPresenter.getTypePositinByTypeId(mGoodsInfo.typeId)
            val goodsTypeInfo = goodsFragment.goodsFragmentPresenter.goodsTypeInfo.get(typePositin)
            var count = goodsTypeInfo.tvRedDotCount
            if (isAdd) {
                count++
            } else {
                count--
            }
            goodsTypeInfo.tvRedDotCount = count
            goodsFragment.goodRvAdapter.notifyDataSetChanged()
            goodsFragment.goodsLvAdapter.notifyDataSetChanged()
        }

        private fun doAddOperation() {
            mGoodsInfo.count++
            notifyDataSetChanged()
            updateRedCountTv(true)
            businessActivity.updateCartUi()
        }
    }
}
