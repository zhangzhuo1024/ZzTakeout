package com.example.zztakeout.dagger2.component

import com.example.zztakeout.dagger2.model.HomeFragmentModel
import com.example.zztakeout.ui.fragment.HomeFragment
import dagger.Component

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/3
 */
@Component(modules = arrayOf(HomeFragmentModel::class))
interface HomeFragmentComponent {
    fun inject(fragment: HomeFragment)
}