package com.example.zztakeout.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.AMapUtils
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.*
import com.example.zztakeout.R
import com.example.zztakeout.utils.OrderObservable
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.json.JSONObject
import java.util.*


class OrderDetailActivity : AppCompatActivity(), Observer {
    lateinit var mapView: MapView
    lateinit var aMap: AMap
    var points :ArrayList<LatLng> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        processIntent()

        mapView = findViewById(R.id.mMapView) as MapView
        mapView.onCreate(savedInstanceState) // 此方法必须重写
        aMap = mapView.map

        ll_order_detail_type_point_container.setOnClickListener{
            markThePosition()
        }
    }
    lateinit var riderMarker : Marker
    private fun markThePosition() {
        mMapView.visibility = View.VISIBLE
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(30.5044010000,114.4834990000),16f))

        aMap.addMarker(MarkerOptions()
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.order_seller_icon))
            .position(LatLng(30.5026820000,114.4835470000))
            .title("天天鲜水果大卖场")
            .snippet("提供各式各样的水果")
        )

        var imageView = ImageView(this)
        imageView.setImageResource(R.drawable.order_buyer_icon)
        aMap.addMarker(MarkerOptions()
            .icon(BitmapDescriptorFactory.fromView(imageView))
            .position(LatLng(30.5044010000,114.4834990000))
            .title("买家位置")
            .snippet("买家买水果啦")
        )

        val handler = Handler()
        handler.postDelayed({
            //骑士登场
            var riderImageView = ImageView(this)
            riderImageView.setImageResource(R.drawable.order_rider_icon)
            riderMarker = aMap.addMarker(MarkerOptions()
                    .position(LatLng(30.5030980000, 114.4841270000))
                    .icon(BitmapDescriptorFactory.fromView(imageView))
                    .title("我是外卖帝骑士")
            )
            points.add(LatLng(30.5030980000, 114.4841270000))
            //  .snippet("我是黑马骑士"))
            riderMarker.showInfoWindow()  //showInfoWindow调用才会显示title和摘要 snippet

        }, 3000)

        Handler().postDelayed({
            //移动骑士  22.5739110000,113.9180200000
            //更新骑手位置就是用新位置重新标记骑手
            var lat = "30.5045010000"
            var lng = "114.4835990000"
            riderMarker.hideInfoWindow()
            riderMarker.position = LatLng(lat.toDouble(),lng.toDouble())
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat.toDouble(),lng.toDouble()), 16f))
            //测距功能
            val distance = AMapUtils.calculateLineDistance(LatLng(lat.toDouble(),lng.toDouble()),
                LatLng(30.5044010000,114.4834990000))
            riderMarker.title = "距离您还有" + Math.abs(distance) + "米"
            riderMarker.showInfoWindow()

            //绘制轨迹
            points.add(LatLng(lat.toDouble(),lng.toDouble()))
            val polyline = aMap.addPolyline(
                PolylineOptions().color(Color.RED).width(3.0f)
                    .add(points.get(points.size -1), points.get(points.size -2)))
        }, 3000)
    }

    lateinit var mOrderId: String
    lateinit var type: String
    private fun processIntent() {
        if (intent.hasExtra("orderId")) {
            mOrderId = intent.getStringExtra("orderId")
            type = intent.getStringExtra("type")
            val index = getIndex(type)
            (ll_order_detail_type_point_container.getChildAt(index) as ImageView).setImageResource(R.drawable.order_time_node_disabled)
            (ll_order_detail_type_container.getChildAt(index) as TextView).setTextColor(Color.BLUE)

        }
    }

    override fun update(observer: Observable?, data: Any?) {
        Log.e("Takeout", " OrderDetailActivity " + data)
        val jsonObject = JSONObject(data as String)
        val orderId = jsonObject.getString("orderId")
        val orderType = jsonObject.getString("orderType")
        if (orderId == mOrderId) {
            type = orderType
            val index = getIndex(type)
            (ll_order_detail_type_point_container.getChildAt(index) as ImageView).setImageResource(R.drawable.order_time_node_disabled)
            (ll_order_detail_type_container.getChildAt(index) as TextView).setTextColor(Color.BLUE)
        }
    }

    private fun getIndex(type: String): Int {
        var index = -1
        when (type) {
            OrderObservable.ORDERTYPE_UNPAYMENT -> {
            }
            OrderObservable.ORDERTYPE_SUBMIT ->  //                typeInfo = "已提交订单";
                index = 0
            OrderObservable.ORDERTYPE_RECEIVEORDER ->  //                typeInfo = "商家接单";
                index = 1
            OrderObservable.ORDERTYPE_DISTRIBUTION,
            OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL,
            OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL,
            OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE ->  //                typeInfo = "配送中";
                index = 2
            OrderObservable.ORDERTYPE_SERVED ->  //                typeInfo = "已送达";
                index = 3
            OrderObservable.ORDERTYPE_CANCELLEDORDER -> {
            }
        }
        return index
    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState)
    }
}
