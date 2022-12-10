package com.example.fastugadriver.gateway

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fastugadriver.data.LoginRepository
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapBoxGateway {

    private val _responseDirections  =  MutableLiveData<Response<DirectionsResponse?>>()
    val responseDirections: LiveData<Response<DirectionsResponse?>> = _responseDirections

    private val _responseGecode  =  MutableLiveData<Response<GeocodingResponse?>>()
    val responseGecode: LiveData<Response<GeocodingResponse?>> = _responseGecode

    private val _responseRoutes  =  MutableLiveData< List<NavigationRoute>>()
    val responseRoutes: LiveData<List<NavigationRoute>> = _responseRoutes


    fun getStaticPath(origin : Point, destination: Point, accessToken: String) {
        val client: MapboxDirections = getClientStaticPathRequest(origin, destination, accessToken)

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

    private fun getClientStaticPathRequest(origin: Point, destination: Point, accessToken: String) : MapboxDirections{
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


    fun findRoute(context: Context, navigationLocationProvider: NavigationLocationProvider, mapboxNavigation: MapboxNavigation) {
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return

        // execute a route request
        // it's recommended to use the
        // applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
        // that make sure the route request is optimized
        // to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(context)
                .coordinatesList(listOf(originPoint, LoginRepository.destinationCoordinates))
                // provide the bearing for the origin of the request to ensure
                // that the returned route faces in the direction of the current user movement
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : NavigationRouterCallback {
                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                    // no impl
                }

                override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
                    // no impl
                }

                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    _responseRoutes.value = routes

                }
            }
        )
    }


    fun getCoordinates(address: String, accessToken: String){
        val client = getClientGecodeRequest(address, accessToken)

        client.enqueueCall(object : Callback<GeocodingResponse?> {
            override fun onResponse(
                call: Call<GeocodingResponse?>,
                response: Response<GeocodingResponse?>,
            ) {
                if (response.body() != null) {
                    _responseGecode.value = response
                }
            }

            override fun onFailure(call: Call<GeocodingResponse?>, throwable: Throwable) {
                println("Geocoding Failure: ${throwable.message}")
            }
        })
    }

    private fun getClientGecodeRequest(address: String, accessToken: String) : MapboxGeocoding{
        return MapboxGeocoding.builder()
            .accessToken(accessToken)
            .query(address)
            .build()
    }



}
