package com.example.fastugadriver.gateway

import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.pojos.*
import com.example.fastugadriver.data.pojos.orders.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderGateway {

    private val _fasTugaResponse  =  MutableLiveData<FasTugaResponse?>()
    val fasTugaResponse: MutableLiveData<FasTugaResponse?> = _fasTugaResponse
   /* private var _orderBody = OrderResponse()
    val orderBody: OrderResponse? = _orderBody*/


    fun getOrders () {
        // calling the method from API to get the Driver logged in
        val call: Call<OrderResponse> = FasTugaAPI.getInterface().getOrders()
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
                //Test data returned
                /*print(response.body()?.current_page)
                print(" order[0]->id: ")
                print(response.body()?.data?.get(0)?.id)
                print(" order[0]->delivered_by: ")
                print(response.body()?.data?.get(0)?.delivered_by)*/
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
                //_orderBody = response.body()!!
            }

            override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    /*fun getOrder(){

        // calling the method from API to get the Driver logged in
        val call: Call<ResponseBody> = FasTugaAPI.getInterface().getDriver()

        // on below line we are executing our method.
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                // Storing logged in driver inside repository
                LoginRepository.driver = FasTugaAPI.convertToClass(response.body()!!.charStream(),
                    LoggedInDriver::class.java) as LoggedInDriver?

                _fasTugaResponse.value = LoginSuccessResponse()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }*/


}