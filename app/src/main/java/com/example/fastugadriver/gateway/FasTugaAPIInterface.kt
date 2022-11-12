package com.example.fastugadriver.gateway

import com.example.fastugadriver.data.model.Driver
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST


interface FasTugaAPIInterface {

    @POST("register/driver")
    fun registerDriver(@Body driver: Driver?): Call<ResponseBody>

}