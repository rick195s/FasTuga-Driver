package com.example.fastugadriver.gateway

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.R

import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapBoxGateway {

    private val _responseDirections  =  MutableLiveData<Response<DirectionsResponse?>>()
    val responseDirections: LiveData<Response<DirectionsResponse?>> = _responseDirections


    fun getRoute(origin : Point, destination: Point, accessToken: String) {
        val client: MapboxDirections = getClientRouteRequest(origin, destination, accessToken)

        client.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>,
            ) {

                println("Response code: " + response.code())
                if (response.body() == null) {
                    println("No routes found, make sure you set the right user and access token.")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    println("No routes found")
                    return
                }

                _responseDirections.value = response
            }

            override fun onFailure(call: Call<DirectionsResponse?>, throwable: Throwable) {
                println("Error: " + throwable.message)
            }
        })
    }

    private fun getClientRouteRequest(origin: Point, destination: Point, accessToken: String) : MapboxDirections{
        return MapboxDirections.builder()
            .routeOptions(
                RouteOptions.builder()
                    .coordinatesList(listOf(
                        origin,
                        destination
                    ))
                    .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
                    .overview(DirectionsCriteria.OVERVIEW_FULL)
                    .build()
            )
            .accessToken(accessToken)
            .build()
    }
}
