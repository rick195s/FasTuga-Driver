package com.example.fastugadriver.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.api.FasTugaFormErrorResponse
import com.example.fastugadriver.data.api.FasTugaResponse
import com.example.fastugadriver.data.api.FasTugaSuccessResponse
import com.example.fastugadriver.data.model.Driver
import com.example.fastugadriver.ui.register.RegisterResult
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class FasTugaAPI {


        val BASE_URI = "https://5cfa-2-80-254-68.ngrok.io/api/"

        private val _fasTugaResponse  =  MutableLiveData<FasTugaResponse>()
        val fasTugaResponse: LiveData<FasTugaResponse> = _fasTugaResponse


    fun getInterface(): FasTugaAPIInterface {
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
             val fastTugaDriverAPI: FasTugaAPIInterface =
                retrofit.create(FasTugaAPIInterface::class.java)

            return fastTugaDriverAPI;


        }

        fun registerDriver(driver :Driver) {
            val fasTugaAPI: FasTugaAPIInterface = getInterface()

            // calling the method from API to register a Driver
            val call: Call<ResponseBody> = fasTugaAPI.registerDriver(driver)

            // on below line we are executing our method.
            call.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {

                    if (!response.isSuccessful){
                        val responseFromAPI: ResponseBody? = response.errorBody()
                        val gson = Gson()

                        try {
                            _fasTugaResponse.value = gson.fromJson(response.errorBody()!!.charStream(),
                                FasTugaFormErrorResponse::class.java)

                        }catch (e: Throwable){
                            println(e.message)
                        }


                        return
                    }

                    _fasTugaResponse.value = FasTugaSuccessResponse()
                }
                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }


}



