package com.example.zztakeout.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zztakeout.R
import com.example.zztakeout.ui.adapter.CartRvAdapter
import com.example.zztakeout.ui.fragment.CommentsFragment
import com.example.zztakeout.ui.fragment.GoodsFragment
import com.example.zztakeout.ui.fragment.SellersFragment
import com.example.zztakeout.utils.PriceFormater
import kotlinx.android.synthetic.main.activity_business.*

class BusinessActivity : AppCompatActivity(), View.OnClickListener {
    var bottomSheetView: View? = null
    lateinit var rvCart: RecyclerView
    lateinit var cartAdapter: CartRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
        vp.adapter = BusinessFragmentAdapter()
        tabs.setupWithViewPager(vp)
        bottom.setOnClickListener(this)
    }

    val fragmentList: List<Fragment> =
        listOf(GoodsFragment(), SellersFragment(), CommentsFragment())
    val titleList = listOf<String>("商品", "商家", "评论")

    inner class BusinessFragmentAdapter : FragmentPagerAdapter(supportFragmentManager) {
        override fun getPageTitle(position: Int): CharSequence? {
            return titleList.get(position)
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList.get(position)
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

    }

    fun addImageButton(ib: ImageButton, width: Int, height: Int) {
        //将这个button添加到activity所占用的布局的区域里面
        fl_Container.addView(ib, width, height)
    }

    fun getCartLocation(): IntArray {
        val cartLocation = IntArray(2)
        imgCart.getLocationInWindow(cartLocation)
        return cartLocation
    }

    fun updateCartUi() {
        var count = 0
        var totalPrice = 0f
        val goodsFragment: GoodsFragment = fragmentList.get(0) as GoodsFragment
        val cartList = goodsFragment.goodsFragmentPresenter.getCartList()
        for (i in 0 until cartList.size) {
            val goodsInfo = cartList.get(i)
            count += goodsInfo.count
            totalPrice += goodsInfo.count * goodsInfo.newPrice.toFloat()
        }
        tvSelectNum.text = count.toString()
        if (count > 0) {
            tvSelectNum.visibility = View.VISIBLE
        } else {
            tvSelectNum.visibility = View.GONE
        }
        tvCountPrice.text = PriceFormater.format(totalPrice)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bottom -> showOrHideCart()
        }

    }

    private fun showOrHideCart() {
        if (bottomSheetView == null) {
            //加载要显示的布局
            bottomSheetView = LayoutInflater.from(this)
                .inflate(R.layout.cart_list, window.decorView as ViewGroup, false)
        }
        rvCart = bottomSheetView!!.findViewById(R.id.rvCart) as RecyclerView
        rvCart.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartRvAdapter(this)
        rvCart.adapter = cartAdapter

        //判断BottomSheetLayout内容是否显示
        if (bottomSheetLayout.isSheetShowing) {
            //关闭内容显示
            bottomSheetLayout.dismissSheet()
        } else {
            //显示BottomSheetLayout里面的内容
            val goodsFragment: GoodsFragment = fragmentList.get(0) as GoodsFragment
            val cartList = goodsFragment.goodsFragmentPresenter.getCartList()
            cartAdapter.setData(cartList)
            if (cartList.size > 0) {
                bottomSheetLayout.showWithSheetView(bottomSheetView)
            }
        }
    }
}
