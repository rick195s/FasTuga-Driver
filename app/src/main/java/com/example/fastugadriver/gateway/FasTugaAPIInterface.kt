package com.example.fastugadriver.gateway

import com.example.fastugadriver.data.pojos.Driver
import com.example.fastugadriver.data.pojos.auth.LoggedInDriver
import com.example.fastugadriver.data.pojos.auth.Token
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface FasTugaAPIInterface {

    // token needs to be inside LoginRepository
    @GET("me")
    fun getDriver(): Call<LoggedInDriver>

    @POST("login/driver")
    fun loginDriver(@Body driver: Driver?): Call<Token>

    // token needs to be inside LoginRepository
    @POST("logout")
    fun logoutDriver(): Call<ResponseBody>

    @POST("register/driver")
    fun registerDriver(@Body driver: Driver?): Call<Token>

    @PUT("drivers/{driver}")
    fun updateDriver(@Path(value="driver") driver_id: Int?, @Body driver: Driver?): Call<LoggedInDriver>
}