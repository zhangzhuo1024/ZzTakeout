package com.example.zztakeout.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.zztakeout.R
import com.example.zztakeout.ui.fragment.CommentsFragment
import com.example.zztakeout.ui.fragment.GoodsFragment
import com.example.zztakeout.ui.fragment.SellersFragment
import kotlinx.android.synthetic.main.activity_business.*

class BusinessActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
        vp.adapter = BusinessFragmentAdapter()
        tabs.setupWithViewPager(vp)
    }

    val fragmentList: List<Fragment> = listOf(GoodsFragment(), SellersFragment(), CommentsFragment())
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
}
