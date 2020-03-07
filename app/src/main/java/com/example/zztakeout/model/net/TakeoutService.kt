package com.example.zztakeout.model.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TakeoutService {
    @GET("home")
    fun getHomeInfo(): Call<ResponseInfo>

    @GET("login")
    fun loginByPhone(@Query("phone") phone: String): Call<ResponseInfo>
}
