package com.example.zztakeout.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.example.zztakeout.R
import com.example.zztakeout.ui.adapter.AroundRvAdapter
import kotlinx.android.synthetic.main.activity_map_location.*
import java.util.ArrayList


class MapLocationActivity : AppCompatActivity(), LocationSource, AMapLocationListener,
    PoiSearch.OnPoiSearchListener {
    lateinit var mapView: MapView
    lateinit var aMap: AMap
    lateinit var aroundRvAdapter: AroundRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_location)

        mapView = findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState) // 此方法必须重写
        aMap = mapView.map
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);


        rv_around.layoutManager = LinearLayoutManager(this)
        aroundRvAdapter = AroundRvAdapter(this);
        rv_around.adapter = aroundRvAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }


    /**
     * 停止定位
     */
    override fun deactivate() {
        Log.e("MapLocationActivity", "停止定位")
    }

    lateinit var mListener: OnLocationChangedListener
    lateinit var mlocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption
    /**
     * 激活定位
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener?) {
        if (listener == null) {
            return
        } else {
            mListener = listener
        }
        //初始化定位
        mlocationClient = AMapLocationClient(this)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位回调监听
        mlocationClient.setLocationListener(this)
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy)
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption)
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation() //启动定位

    }

    /**
     * 定位成功后回调函数
     */
    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() === 0) {
                mListener.onLocationChanged(aMapLocation) // 显示系统小蓝点
                //移动地图到当前位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(aMapLocation.latitude, aMapLocation.longitude)))
                aMap.moveCamera(CameraUpdateFactory.zoomTo(16f))
                doSearchQuery(aMapLocation)
            } else {
                val errText =
                    "定位失败," + aMapLocation.getErrorCode().toString() + ": " + aMapLocation.getErrorInfo()
                Log.e("AmapErr", errText)
            }
        }
    }

    //查询最近的地址位置，在onPoiSearched的回调中返回查询结果
    private fun doSearchQuery(aMapLocation: AMapLocation) {
        val query = PoiSearch.Query("", "", aMapLocation.city)
        //keyWord表示搜索字符串，
        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.pageSize = 30// 设置每页最多返回多少条poiitem
        query.pageNum = 1//设置查询页码
        val poiSearch = PoiSearch(this, query)
        //搜索范围
        poiSearch.bound =
            PoiSearch.SearchBound(LatLonPoint(aMapLocation.latitude, aMapLocation.longitude), 350)
        poiSearch.setOnPoiSearchListener(this)
        poiSearch.searchPOIAsyn();

    }

    override fun onPoiItemSearched(poiItem: PoiItem?, rcode: Int) {

    }

    override fun onPoiSearched(poiResult: PoiResult?, rcode: Int) {
        if (rcode == 1000) {
            if (poiResult != null) {
                val poiItems: ArrayList<PoiItem> = poiResult.pois!!
                aroundRvAdapter.setData(poiItems)
            }
        }
    }
}
