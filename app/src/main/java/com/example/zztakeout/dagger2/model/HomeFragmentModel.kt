package com.example.zztakeout.dagger2.model

import com.example.zztakeout.presenter.HomeFragmentPresenter
import com.example.zztakeout.ui.fragment.HomeFragment
import dagger.Module
import dagger.Provides

/**
 * Created by zhangzhuo.
 * Blog: https://blog.csdn.net/zhangzhuo1024
 * Date: 2020/3/3
 */
@Module
class HomeFragmentModel(val homeFragment: HomeFragment) {
    @Provides
    fun providerHomeFragmentPresenter(): HomeFragmentPresenter {
        return HomeFragmentPresenter(homeFragment)
    }
}