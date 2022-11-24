package com.example.fastugadriver.gateway

import com.example.fastugadriver.data.pojos.Driver
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST


interface FasTugaAPIInterface {

    @POST("login/driver")
    fun loginDriver(@Body driver: Driver?): Call<ResponseBody>

    @POST("register/driver")
    fun registerDriver(@Body driver: Driver?): Call<ResponseBody>

}