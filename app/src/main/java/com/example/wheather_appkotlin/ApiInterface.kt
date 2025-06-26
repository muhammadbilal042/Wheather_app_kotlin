package com.example.wheather_appkotlin

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    fun getwheatherdata(
        @Query ("q") city:String,
        @Query ("appid") appid:String,
        @Query ("units") units:String
    ) : retrofit2.Call<Weatherapp>
}