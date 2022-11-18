package com.example.fastugadriver.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.api.FasTugaFormErrorResponse
import com.example.fastugadriver.data.api.FasTugaLoginSuccessResponse
import com.example.fastugadriver.data.api.FasTugaResponse
import com.example.fastugadriver.data.model.Driver
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Reader


class FasTugaAPI {

    private val BASE_URI = "http://10.0.2.2/api/"

    private val _fasTugaResponse  =  MutableLiveData<FasTugaResponse>()
    val fasTugaResponse: LiveData<FasTugaResponse> = _fasTugaResponse

    private var fasTugaAPIInterface: FasTugaAPIInterface = getInterface()

    private fun getInterface(): FasTugaAPIInterface {
            val clientBuilder : OkHttpClient.Builder= OkHttpClient.Builder()

            clientBuilder.addInterceptor(Interceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder().header("Accept", "application/json").build()
                chain.proceed(newRequest)
            })

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build()

            // below line is to create an instance for our retrofit api class.
          return retrofit.create(FasTugaAPIInterface::class.java)
        }

        fun registerDriver(driver :Driver) {
            // calling the method from API to register a Driver
            val call: Call<ResponseBody> = fasTugaAPIInterface.registerDriver(driver)

            // on below line we are executing our method.
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                    if (!response.isSuccessful){
                            _fasTugaResponse.value = convertToClass( response.errorBody()!!.charStream(),
                                FasTugaFormErrorResponse::class.java) as FasTugaFormErrorResponse
                        return
                    }

                    _fasTugaResponse.value = convertToClass(response.body()!!.charStream(),
                        FasTugaLoginSuccessResponse::class.java) as FasTugaLoginSuccessResponse?
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

    private fun convertToClass(json: Reader, convertTo: Class<*>): Any? {
        val gson = Gson()

        try {
            return gson.fromJson(json,
                convertTo)

        }catch (e: Throwable){
            println(e.message)
        }
        return null
    }

}



