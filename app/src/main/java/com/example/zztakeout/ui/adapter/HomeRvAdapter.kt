package com.example.zztakeout.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.example.zztakeout.R
import com.example.zztakeout.R.id.slider
import com.example.zztakeout.model.bean.Seller
import com.squareup.picasso.Picasso

class HomeRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mDatas: ArrayList<Seller> = ArrayList<Seller>()

    fun setData(data: ArrayList<Seller>) {
        mDatas = data
        notifyDataSetChanged()
    }

    companion object {
        val TYPE_TITLE = 0
        val TYPE_SELLER = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_TITLE
        } else {
            return TYPE_SELLER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_TITLE -> return TitleHolder(View.inflate(context, R.layout.item_title, null))
            TYPE_SELLER -> return SellerHolder(View.inflate(context, R.layout.item_seller, null))
            else -> return SellerHolder(View.inflate(context, R.layout.item_seller, null))
        }
    }

    override fun getItemCount(): Int {
        if (mDatas.size > 0) {
            return mDatas.size + 1
        } else {
            return 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_TITLE -> (holder as TitleHolder).bindData()
            TYPE_SELLER -> (holder as SellerHolder).bindData(mDatas[position - 1])
        }
    }


    inner class TitleHolder(item: View) : RecyclerView.ViewHolder(item) {
        var mSlider: SliderLayout

        init {
            mSlider = item.findViewById<SliderLayout>(slider)

        }

        private val hashMap = HashMap<String, String>()

        fun bindData() {

            if (hashMap.size == 0) {
                hashMap.put(
                    "猛男的炒饭",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583074381272&di=72d7ac174c68f1663241abc3c9a70c93&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170628%2F096056bbc12947a18bcbb1f23a5bf4fc_th.jpg"
                )
                hashMap.put(
                    "签串儿",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583074381266&di=75113a713b86c2c808e4e99cbadbc459&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn%2Fw2000h1333%2F20171218%2Fdcb6-fypsqka6891531.jpg"
                )
                hashMap.put(
                    "肯德基",
                    "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1438834571,4037809350&fm=26&gp=0.jpg"
                )
                hashMap.put(
                    "三文鱼",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1583074381258&di=f4eb34270791b3a627fdc21a63345e9f&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161201%2F0eef1c4cacbb4dbca3cc842bec8c989c_th.jpeg"
                )
                for ((key, value) in hashMap) {
                    val textSliderView = TextSliderView(context)
                    textSliderView.description(key).image(value)
                    mSlider.addSlider(textSliderView)
                }
            }
        }
    }

    inner class SellerHolder(item: View) : RecyclerView.ViewHolder(item) {
        val tvTitle: TextView
        val ivLogo: ImageView
        val rbScore: RatingBar
        val tvSale: TextView
        val tvSendPrice: TextView
        val tvDistance: TextView
        lateinit var mSeller: Seller

        init {
            tvTitle = item.findViewById(R.id.tv_title)
            ivLogo = item.findViewById(R.id.seller_logo)
            rbScore = item.findViewById(R.id.ratingBar)

            tvSale = item.findViewById(R.id.tv_home_sale)
            tvSendPrice = item.findViewById(R.id.tv_home_send_price)
            tvDistance = item.findViewById(R.id.tv_home_distance)
        }

        fun bindData(seller: Seller) {
            this.mSeller = seller
            tvTitle.text = seller.name
            Picasso.with(context).load(seller.icon).into(ivLogo)
            rbScore.rating = seller.score.toFloat()
            tvSale.text = "月售${seller.sale}单"
            tvSendPrice.text = "￥${seller.sendPrice}起送/配送费￥${seller.deliveryFee}"
            tvDistance.text = seller.distance
        }
    }

}
