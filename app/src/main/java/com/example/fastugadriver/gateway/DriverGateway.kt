package com.example.fastugadriver.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.LoginSuccessResponse
import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.example.fastugadriver.data.pojos.Driver
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverGateway {

    private val _fasTugaResponse  =  MutableLiveData<FasTugaResponse>()
    val fasTugaResponse: LiveData<FasTugaResponse> = _fasTugaResponse

    fun loginDriver(driver : Driver) {
        // calling the method from API to register a Driver
        val call: Call<ResponseBody> = FasTugaAPI.getInterface().loginDriver(driver)

        // on below line we are executing our method.
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                _fasTugaResponse.value = FasTugaAPI.convertToClass(response.body()!!.charStream(),
                    LoginSuccessResponse::class.java) as LoginSuccessResponse?
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    fun registerDriver(driver : Driver) {
        // calling the method from API to register a Driver
        val call: Call<ResponseBody> = FasTugaAPI.getInterface().registerDriver(driver)

        // on below line we are executing our method.
        call.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                _fasTugaResponse.value = FasTugaAPI.convertToClass(response.body()!!.charStream(),
                    LoginSuccessResponse::class.java) as LoginSuccessResponse?
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}