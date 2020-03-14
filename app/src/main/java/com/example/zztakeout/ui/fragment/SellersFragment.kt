package com.example.zztakeout.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/14
 */
class SellersFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var sellerView = TextView(activity)
        sellerView.textSize = 30f
        sellerView.text = "商家"
        sellerView.setTextColor(Color.BLACK)
        sellerView.gravity = Gravity.CENTER
        return sellerView
    }
}