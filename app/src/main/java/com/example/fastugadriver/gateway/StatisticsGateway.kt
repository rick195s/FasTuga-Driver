package com.example.fastugadriver.gateway

import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.pojos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatisticsGateway {

    private val _fasTugaResponse  =  MutableLiveData<FasTugaResponse?>()
    val fasTugaResponse: MutableLiveData<FasTugaResponse?> = _fasTugaResponse



    fun getStats (driver: Int?) {
        val call: Call<Statistics> = FasTugaAPI.getInterface().getDriverStats(driver)

        // on below line we are executing our method.
        call.enqueue(object : Callback<Statistics?> {
            override fun onResponse(
                call: Call<Statistics?>,
                response: Response<Statistics?>

            ) {

                if (!response.isSuccessful){
                    _fasTugaResponse.value = FasTugaAPI.convertToClass( response.errorBody()!!.charStream(),
                        FormErrorResponse::class.java) as FormErrorResponse
                    return
                }

                _fasTugaResponse.value = response.body()
            }

            override fun onFailure(call: Call<Statistics?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


}