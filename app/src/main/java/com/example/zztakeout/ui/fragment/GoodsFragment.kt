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
class GoodsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var textView = TextView(activity)
        textView.textSize = 30f
        textView.text = "商品"
        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.CENTER
        return textView
    }
}