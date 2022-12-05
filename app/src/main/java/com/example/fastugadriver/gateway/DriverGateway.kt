package com.example.fastugadriver.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.*
import com.example.fastugadriver.data.pojos.auth.LoggedInDriver
import com.example.fastugadriver.data.pojos.auth.LoginSuccessResponse
import com.example.fastugadriver.data.pojos.auth.LogoutSuccessResponse
import com.example.fastugadriver.data.pojos.auth.Token
import com.example.fastugadriver.data.pojos.orders.OrderResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


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
                _fasTugaResponse.value = FormErrorResponse("")
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
                _fasTugaResponse.value = FormErrorResponse("")
                call.cancel()
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

    fun logoutDriver(){
        // calling the method from API to get the Driver logged in
        val call: Call<ResponseBody> = FasTugaAPI.getInterface().logoutDriver()

        // on below line we are executing our method.
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                // Logging out driver inside repository
                LoginRepository.logout()
                _fasTugaResponse.value = LogoutSuccessResponse()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _fasTugaResponse.value = FormErrorResponse("")
                call.cancel()
            }
        })
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
                _fasTugaResponse.value = FormErrorResponse("")
                call.cancel()
            }
        })
    }

    fun updateDriver(driverId: Int?, driver: Driver?, photo: File?){
        if (driver == null || driverId == null){
            return
        }

        var newPhotoPart : MultipartBody.Part? = null
        if (photo != null){

            val requestFile: RequestBody = photo.asRequestBody("image/*".toMediaTypeOrNull())
            // MultipartBody.Part is used to send also the actual file name
            newPhotoPart = MultipartBody.Part.createFormData("photo", photo.name, requestFile)
        }

        val method = MultipartBody.Part.createFormData("_method", "PUT")

        // calling the method from API to get the Driver logged in
        val call: Call<LoggedInDriver> = FasTugaAPI.getInterface().updateDriver(driverId, driver.toMap(), newPhotoPart, method)

        // on below line we are executing our method.
        call.enqueue(object : Callback<LoggedInDriver> {
            override fun onResponse(call: Call<LoggedInDriver>, response: Response<LoggedInDriver>) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                // Storing logged in driver inside repository
                response.body()?.let { LoginRepository.setDriver(it) }
                _fasTugaResponse.value = SuccessResponse()
            }

            override fun onFailure(call: Call<LoggedInDriver>, t: Throwable) {
                _fasTugaResponse.value = FormErrorResponse(t.message.toString())
                call.cancel()

            }
        })
    }

    fun getDriverOrders (driver: Int?, page:Int? = 0) {
        // calling the method from API to get the Driver logged in
        val call: Call<OrderResponse> = FasTugaAPI.getInterface().getDriverOrders(driver, page)

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


                _fasTugaResponse.value = response.body()
                //_orderBody = response.body()!!
            }

            override fun onFailure(call: Call<OrderResponse?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}