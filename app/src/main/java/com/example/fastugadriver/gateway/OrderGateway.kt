package com.example.fastugadriver.gateway

import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.pojos.*
import com.example.fastugadriver.data.pojos.orders.OrderResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderGateway {

    private val _fasTugaResponse  =  MutableLiveData<FasTugaResponse?>()
    val fasTugaResponse: MutableLiveData<FasTugaResponse?> = _fasTugaResponse
   /* private var _orderBody = OrderResponse()
    val orderBody: OrderResponse? = _orderBody*/


    fun getOrders (filter:String, page:Int? = 0) {
        // calling the method from API to get the Driver logged in
        val call: Call<OrderResponse> = FasTugaAPI.getInterface().getOrders(filter,page)
        val orderBody = null

        // on below line we are executing our method.
        call.enqueue(object : Callback<OrderResponse?> {
            override fun onResponse(
                call: Call<OrderResponse?>,
                response: Response<OrderResponse?>

            ) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }
                /** Represented Structure
                 *{
                "current_page": 1,
                "data": [Order],
                "first_page_url": "http://localhost/api/orders?page=1",
                "from": 1,
                "last_page": 573,
                "last_page_url": "http://localhost/api/orders?page=573",
                "links": [LinksPaginate],
                "next_page_url": "http://localhost/api/orders?page=2",
                "path": "http://localhost/api/orders",
                "per_page": 15,
                "prev_page_url": "",
                "to": 15,
                "total": 8583
                }*/

                _fasTugaResponse.value = response.body()
            }

            override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun cancelOrder () {
        // calling the method from API to get the Driver logged in
        val call: Call<FasTugaResponse> = FasTugaAPI.getInterface().cancelOrder()


        // on below line we are executing our method.
        call.enqueue(object : Callback<FasTugaResponse?> {
            override fun onResponse(
                call: Call<FasTugaResponse?>,
                response: Response<FasTugaResponse?>

            ) {
                if(response.isSuccessful){
                    _fasTugaResponse.value = SuccessResponse()
                    return
                }

                _fasTugaResponse.value = FormErrorResponse()
            }

            override fun onFailure(call: Call<FasTugaResponse?>, t: Throwable) {
                _fasTugaResponse.value = FormErrorResponse()
            }
        })
    }


    fun updateOrderDeliveredBy(order_id: Int?) {
        // calling the method from API to get the Driver logged in
        val call: Call<ResponseBody> = FasTugaAPI.getInterface().updateOrderDeliveredBy(order_id)

        // on below line we are executing our method.
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>

            ) {
                if(response.isSuccessful){
                    _fasTugaResponse.value = SuccessResponse()
                    return
                }

                _fasTugaResponse.value = FormErrorResponse()

            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                _fasTugaResponse.value = FormErrorResponse(t.message)
            }
        })
    }


}