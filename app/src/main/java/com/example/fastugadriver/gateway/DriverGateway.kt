package com.example.fastugadriver.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.*
import kotlinx.coroutines.*
import okhttp3.Dispatcher
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

                val token: Token = FasTugaAPI.convertToClass(response.body()!!.charStream(),
                    Token::class.java) as Token

                LoginRepository.setToken(token)
                getDriver()
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

                val token: Token = FasTugaAPI.convertToClass(response.body()!!.charStream(),
                    Token::class.java) as Token

                LoginRepository.setToken(token)
                getDriver()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                throw t
            }
        })
    }

    fun getDriver(){

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
    }
}