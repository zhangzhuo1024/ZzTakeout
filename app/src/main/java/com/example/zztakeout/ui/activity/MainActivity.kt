package com.example.zztakeout.ui.activity

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.zztakeout.R
import com.example.zztakeout.ui.fragment.HomeFragment
import com.example.zztakeout.ui.fragment.MoreFragment
import com.example.zztakeout.ui.fragment.OrderFragment
import com.example.zztakeout.ui.fragment.UserFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val fragmentList: List<Fragment> = listOf(HomeFragment(), OrderFragment(), UserFragment(), MoreFragment())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ll_main_activity.setPadding(0, 0, 0, getNavigationBarHeight(this).dp2px())

        initBottom()


    }

    private fun initBottom() {
        changeIndex(0)
        for (i in 0 until main_bottom_bar.childCount) {
            main_bottom_bar.getChildAt(i).setOnClickListener {
                changeIndex(i)
            }
        }
    }

    private fun changeIndex(index: Int) {
        for (i in 0 until main_bottom_bar.childCount) {
            if (index == i) {
                setChildEnable(main_bottom_bar.getChildAt(i), false)
            } else {
                setChildEnable(main_bottom_bar.getChildAt(i), true)
            }
        }
        fragmentManager.beginTransaction().replace(R.id.main_content, fragmentList.get(index)).commit()
    }

    private fun setChildEnable(childView: View, isEnable: Boolean) {
        childView.isEnabled = isEnable
        if (childView is ViewGroup) {
            for (index in 0 until childView.childCount){
                childView.getChildAt(index).isEnabled = isEnable
            }
        }
    }

    fun Int.dp2px(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, toFloat(), resources.displayMetrics
        ).toInt()
    }

    /**
     * 获取导航栏高度 ,此方法不会检查导航栏是否存在，直接返回数值。所以可能手机没有显示导航栏，但是高度依然返回	  * @param activity	  * @return
     */
    fun getNavigationBarHeight(activity: Context): Int {
        val resources = activity.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        //获取NavigationBar的高度			i
        return resources.getDimensionPixelSize(resourceId)
    }
}
