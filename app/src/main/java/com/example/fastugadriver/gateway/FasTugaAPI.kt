package com.example.fastugadriver.gateway

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Reader


class FasTugaAPI {

    companion object{

         val BASE_URI = "http://10.0.2.2/api/"

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
            return retrofit.create(FasTugaAPIInterface::class.java)
        }

        fun convertToClass(json: Reader, convertTo: Class<*>): Any? {
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

}



