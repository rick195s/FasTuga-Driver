package com.example.fastugadriver.gateway

import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.Driver
import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.example.fastugadriver.data.pojos.Statistics
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

    @GET("orders/drivers")
    fun getOrders( @Query(value="filter")  filter: String?, @Query("page") page: Int?): Call<OrderResponse>

    @GET("drivers/{driver}/orders")
    fun getDriverOrders(@Path(value="driver") driver_id: Int?, @Query("page") page: Int? ): Call<OrderResponse>

    @GET("drivers/{driver}/statistics")
    fun getDriverStats(@Path(value="driver") driver_id: Int?): Call<Statistics>

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

    @FormUrlEncoded
    @PUT("orders/{order}")
    fun cancelOrder(@Path(value="order") order_id: Int? = LoginRepository.selectedOrder?.id,
                    @Field("status") status: String = "C"): Call<ResponseBody>

    @FormUrlEncoded
    @PUT("orders/{order}")
    fun updateOrderDeliveredBy(@Path(value="order") order_id: Int?,
                               @Field("delivered_by") delivered_by: String = LoginRepository.driver?.id.toString()): Call<ResponseBody>

    @PUT("orders/drivers/{order}/start_delivery")
    fun startDeliveryOrder(@Path(value="order") order_driver_delivery_id: Int = LoginRepository.selectedOrder?.order_driver_delivery_id!!): Call<ResponseBody>

    @PUT("orders/drivers/{order}/end_delivery")
    fun endDeliveryOrder(@Path(value="order") order_driver_delivery_id: Int = LoginRepository.selectedOrder?.order_driver_delivery_id!!): Call<ResponseBody>

}