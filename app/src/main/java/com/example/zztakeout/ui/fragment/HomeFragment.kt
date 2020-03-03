package com.example.zztakeout.ui.fragment

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.zztakeout.R
import com.example.zztakeout.dagger2.component.DaggerHomeFragmentComponent
import com.example.zztakeout.dagger2.model.HomeFragmentModel
import com.example.zztakeout.model.bean.Seller
import com.example.zztakeout.presenter.HomeFragmentPresenter
import com.example.zztakeout.ui.adapter.HomeRvAdapterer
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment() {
    lateinit var rvHome: RecyclerView
    lateinit var homeRvAdapterer: HomeRvAdapterer
    @Inject
    lateinit var homeFragmentPresenter: HomeFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = View.inflate(activity, R.layout.fragment_home, null)
        rvHome = view.findViewById<RecyclerView>(R.id.rv_home)
        rvHome.layoutManager = LinearLayoutManager(activity)
        homeRvAdapterer = HomeRvAdapterer(activity)

        rvHome.adapter = homeRvAdapterer
//        homeFragmentPresenter = HomeFragmentPresenter(this)
        DaggerHomeFragmentComponent.builder().homeFragmentModel(HomeFragmentModel(this)).build().inject(this)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("Takeout", " HomeFragment " + "onActivityCreated")
        initData()
    }

    var sum: Int = 0
    var distance: Int = 0
    var alpha = 55
    private fun initData() {
        Log.e("Takeout", " HomeFragment " + "initData")

        homeFragmentPresenter.getHomeInfo()

        addScrollListener()
    }

    private fun addScrollListener() {
        distance = 120.dp2px()
        rvHome.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                sum += dy
                if (sum > distance) {
                    alpha = 255
                } else {
                    alpha = sum * 200 / distance
                    alpha += 55
                }
                ll_title_container.setBackgroundColor(Color.argb(alpha, 0x31, 0x90, 0xe8))
            }
        })
    }

    fun Int.dp2px(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).toInt()

    }

    var mData: ArrayList<Seller> = ArrayList<Seller>()
    fun onHomeSuccess(nearbySellers: List<Seller>, otherSellers: List<Seller>) {
        mData.clear()
        mData.addAll(nearbySellers)
        mData.addAll(otherSellers)
        homeRvAdapterer.setData(mData)
    }

    fun onHomeFail() {


    }
}
