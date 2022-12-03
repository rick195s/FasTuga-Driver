package com.example.fastugadriver.gateway

import com.example.fastugadriver.data.pojos.Driver
import com.example.fastugadriver.data.pojos.auth.LoggedInDriver
import com.example.fastugadriver.data.pojos.auth.Token
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import com.example.fastugadriver.data.pojos.orders.OrderResponse
import retrofit2.Call
import retrofit2.http.*


interface FasTugaAPIInterface {

    // token needs to be inside LoginRepository
    @GET("me")
    fun getDriver(): Call<LoggedInDriver>

    @GET("orders/driver")
    fun getOrders(@Query("page") page: Int? ): Call<OrderResponse>

    @POST("login/driver")
    fun loginDriver(@Body driver: Driver?): Call<Token>

    // token needs to be inside LoginRepository
    @POST("logout")
    fun logoutDriver(): Call<ResponseBody>

    @POST("register/driver")
    fun registerDriver(@Body driver: Driver?): Call<Token>

    @Multipart
    @POST("drivers/{driver}")
    fun updateDriver(@Path(value="driver") driver_id: Int?, @PartMap  partMap: Map<String, @JvmSuppressWildcards RequestBody>,
                     @Part photo: MultipartBody.Part?, @Part method: MultipartBody.Part): Call<LoggedInDriver>
}