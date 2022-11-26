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
        val call: Call<Token> = FasTugaAPI.getInterface().loginDriver(driver)

        // on below line we are executing our method.
        call.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {


                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                login(token = response.body())
            }

            override fun onFailure(call: Call<Token?>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }


    fun registerDriver(driver : Driver) {
        // calling the method from API to register a Driver
        val call: Call<Token> = FasTugaAPI.getInterface().registerDriver(driver)

        // on below line we are executing our method.
        call.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                login(response.body())

            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                call.cancel()
                throw t

            }
        })
    }

    private fun login(token: Token? = null, driver: LoggedInDriver? = null){
        if (token != null) {
            LoginRepository.setToken(token)
            getDriver()
        }

        if (driver != null ){
            LoginRepository.setDriver(driver)
        }
    }

    fun getDriver(){

        // calling the method from API to get the Driver logged in
        val call: Call<LoggedInDriver> = FasTugaAPI.getInterface().getDriver()

        // on below line we are executing our method.
        call.enqueue(object : Callback<LoggedInDriver> {
            override fun onResponse(call: Call<LoggedInDriver>, response: Response<LoggedInDriver>) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                // Storing logged in driver inside repository
                login(driver = response.body())
                _fasTugaResponse.value = LoginSuccessResponse()
            }

            override fun onFailure(call: Call<LoggedInDriver>, t: Throwable) {
                t.printStackTrace()
                call.cancel()
            }
        })
    }
}