package com.example.fastugadriver.gateway

import com.example.fastugadriver.data.pojos.Driver
import com.example.fastugadriver.data.pojos.auth.LoggedInDriver
import com.example.fastugadriver.data.pojos.auth.Token
import okhttp3.ResponseBody
import com.example.fastugadriver.data.pojos.OrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

import retrofit2.http.POST


interface FasTugaAPIInterface {

    // token needs to be inside LoginRepository
    @GET("me")
    fun getDriver(): Call<LoggedInDriver>

    @GET("orders")
    fun getOrders(): Call<OrderResponse>

    @POST("login/driver")
    fun loginDriver(@Body driver: Driver?): Call<Token>

    // token needs to be inside LoginRepository
    @POST("logout")
    fun logoutDriver(): Call<ResponseBody>

    @POST("register/driver")
    fun registerDriver(@Body driver: Driver?): Call<Token>
}