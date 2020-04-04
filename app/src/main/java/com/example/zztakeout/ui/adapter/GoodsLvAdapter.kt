package com.example.zztakeout.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.zztakeout.R
import com.example.zztakeout.model.bean.GoodsInfo
import com.example.zztakeout.ui.activity.BusinessActivity
import com.example.zztakeout.ui.fragment.GoodsFragment
import com.example.zztakeout.utils.PriceFormater
import com.squareup.picasso.Picasso
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter
import java.util.ArrayList

class GoodsLvAdapter(val goodsFragment: GoodsFragment) : BaseAdapter(), StickyListHeadersAdapter {

    companion object{
        val ANIMATION_DURATION = 500L
    }

    var mDatas: List<GoodsInfo> = ArrayList<GoodsInfo>()

    fun setData(allGoodsList: ArrayList<GoodsInfo>) {
        mDatas = allGoodsList
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
        var itemView : View
        var goodsItemHolder:GoodsItemHolder
        if (convertView == null) {
            itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_goods, parent, false)
            goodsItemHolder = GoodsItemHolder(itemView)
            itemView.tag = goodsItemHolder
        } else {
            itemView = convertView
            goodsItemHolder = itemView.tag as GoodsItemHolder
        }
        goodsItemHolder.bindData(mDatas.get(position))
        return itemView
    }

    inner class GoodsItemHolder(val itemView: View) : View.OnClickListener {

        private lateinit var goodsInfo: GoodsInfo
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

        fun bindData(goodsInfo: GoodsInfo) {
            this.goodsInfo = goodsInfo
            tvName.text = goodsInfo.name
            Picasso.with(itemView.context).load(goodsInfo.icon).into(ivIcon)
            tvForm.text = goodsInfo.form
            tvMonthSale.text = "月售${goodsInfo.monthSaleNum}份"
            tvNewPrice.text = PriceFormater.format(goodsInfo.newPrice.toFloat())
            tvOldPrice.text = PriceFormater.format(goodsInfo.oldPrice.toFloat())
            tvCount.text = goodsInfo.count.toString()
            if (goodsInfo.count > 0) {
                tvCount.visibility = View.VISIBLE
                btnMinus.visibility = View.VISIBLE
            } else {
                tvCount.visibility = View.INVISIBLE
                btnMinus.visibility = View.INVISIBLE
            }

        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.ib_add -> doAddOperation()

                R.id.ib_minus -> doMinusOperation()
            }
            val businessActivity = goodsFragment.activity as BusinessActivity
            businessActivity.updateCartUi()
        }

        private fun doAddOperation() {
            //1、按加号时，减号和数量控件出现动画，数量变化
            var count = goodsInfo.count
            if (count == 0) {
                val showAnimatin = getShowAnimatin()
                tvCount.startAnimation(showAnimatin)
                btnMinus.startAnimation(showAnimatin)
            }
            count += 1
            goodsInfo.count = count
            //2、左侧的分类区要添加已选的菜品数量
            updateRedCountTv(true)

            //3、添加时加号图标的抛物线动画，购物车的数量变化，通过克隆加号，添加到activity上保证动画执行时不会收到viewpager滑动的影响
            AnimationToCart()

            notifyDataSetChanged()
        }

        private fun AnimationToCart() {
            val ib = ImageButton(itemView.context)
            var startLocation = IntArray(2)
            btnAdd.getLocationInWindow(startLocation)
            ib.x = startLocation[0].toFloat()
            ib.y = startLocation[1].toFloat()
            ib.setBackgroundResource(R.drawable.button_add)
            val businessActivity = goodsFragment.activity as BusinessActivity
            businessActivity.addImageButton(ib, btnAdd.width, btnAdd.height)
            val cartLocation = businessActivity.getCartLocation()
            startParabolaAnimation(ib, startLocation, cartLocation)
        }

        private fun doMinusOperation() {
            var count = goodsInfo.count
            if (count == 1) {
                val hideAnimatin = getHideAnimatin()
                tvCount.startAnimation(hideAnimatin)
                btnMinus.startAnimation(hideAnimatin)
            }
            count -= 1
            goodsInfo.count = count
            updateRedCountTv(false)
            notifyDataSetChanged()
        }

        private fun updateRedCountTv(isAdd: Boolean) {

            val typePositin =
                goodsFragment.goodsFragmentPresenter.getTypePositinByTypeId(goodsInfo.typeId)
            val goodsTypeInfo = goodsFragment.goodsFragmentPresenter.goodsTypeInfo.get(typePositin)
            var count = goodsTypeInfo.tvRedDotCount
            if (isAdd) {
                count++
            } else {
                count--
            }
            goodsTypeInfo.tvRedDotCount = count
            goodsFragment.goodRvAdapter.notifyDataSetChanged()
        }

        private fun getShowAnimatin(): AnimationSet{
            var animationSet = AnimationSet(false)
            val alphaAnimation = AlphaAnimation(0f, 1f)
            val rotateAnimation = RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
            animationSet.addAnimation(alphaAnimation)
            animationSet.addAnimation(rotateAnimation)
            animationSet.addAnimation(translateAnimation)
            animationSet.duration = ANIMATION_DURATION
            return animationSet
        }

        private fun getHideAnimatin(): Animation {
            var animationSet = AnimationSet(false)
            val alphaAnimation = AlphaAnimation(1f, 0f)
            val rotateAnimation = RotateAnimation(720f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 2.0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f)
            animationSet.addAnimation(alphaAnimation)
            animationSet.addAnimation(rotateAnimation)
            animationSet.addAnimation(translateAnimation)
            animationSet.duration = ANIMATION_DURATION
            return animationSet
        }


        private fun startParabolaAnimation(ib: ImageButton, startLocation: IntArray, cartLocation: IntArray) {
            val translateAnimationX = TranslateAnimation(Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, cartLocation[0].toFloat() - startLocation[0].toFloat(), Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f)
            val translateAnimationY = TranslateAnimation(Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE, 0f, Animation.ABSOLUTE,  cartLocation[1].toFloat() - startLocation[1].toFloat())
            translateAnimationY.setInterpolator(AccelerateInterpolator())
            val animationSet = AnimationSet(false)
            animationSet.addAnimation(translateAnimationX)
            animationSet.addAnimation(translateAnimationY)
            animationSet.duration = ANIMATION_DURATION
            animationSet.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
//                    ib.visibility = View.GONE   addview之后要remove view  不然会窗体泄漏卡顿
                    (ib.parent as ViewGroup).removeView(ib)
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
            ib.startAnimation(animationSet)
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
