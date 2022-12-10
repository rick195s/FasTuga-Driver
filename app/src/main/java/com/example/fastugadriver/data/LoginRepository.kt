package com.example.fastugadriver.data

import android.content.Context
import android.content.SharedPreferences
import com.example.fastugadriver.data.pojos.Statistics
import com.example.fastugadriver.data.pojos.auth.LoggedInDriver
import com.example.fastugadriver.data.pojos.auth.Token
import com.example.fastugadriver.data.pojos.orders.Order
import com.google.gson.Gson
import com.mapbox.geojson.Point

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

// Storing Objects in sharedPreferences in JSON using GSON
object LoginRepository {

    private lateinit var sp: SharedPreferences

    fun init (context : Context) {
        sp = context.getSharedPreferences("login", Context.MODE_PRIVATE)

        val gson = Gson()
        loadToken(gson)
        loadDriver(gson)
        loadOrder(gson)
    }

    // in-memory cache of the loggedInUser object
    var driver: LoggedInDriver? = null
        private set

    var token: Token? = null
        private set

    val isLoggedIn: Boolean
        get() = driver != null && token != null

    var selectedOrder: Order? = null
        private set

    var selectedStats: Statistics? = null
        private set

    var destinationCoordinates: Point? = null

    fun setOrder(selectedOrder: Order?){
        destinationCoordinates = null
        this.selectedOrder = selectedOrder

        val gson = Gson()
        val json:String = gson.toJson(this.selectedOrder)

        with (sp.edit()) {
            putString("order", json)
            apply()
        }
    }

    private fun loadOrder(gson: Gson){
        val orderJSON = sp.getString("order", null)
        if (orderJSON != null){
            selectedOrder =  gson.fromJson(orderJSON, Order::class.java)
        }
    }

    private fun loadStats(gson: Gson){
        val statsJSON = sp.getString("stat", null)
        if (statsJSON != null){
            selectedStats =  gson.fromJson(statsJSON, Statistics::class.java)
        }
    }

    fun logout() {
        driver = null
        token = null

        with (sp.edit()) {
            remove("token")
            remove("driver")
            apply()
        }
    }

    fun setToken(newToken: Token){
        newToken.accessToken = "Bearer ${newToken.accessToken}"
        token = newToken

        val gson = Gson()
        val json:String = gson.toJson(token)

        with (sp.edit()) {
            putString("token", json)
            apply()
        }
    }

    fun setDriver(driver: LoggedInDriver){
        this.driver = driver

        val gson = Gson()
        val json:String = gson.toJson(this.driver)

        with (sp.edit()) {
            putString("driver", json)
            apply()
        }
    }

    private fun loadToken(gson: Gson){
        val tokenJSON = sp.getString("token", null)
        if (tokenJSON != null){
            token =  gson.fromJson(tokenJSON, Token::class.java)
        }
    }

    private fun loadDriver(gson: Gson){
        val driverJSON = sp.getString("driver", null)
        if (driverJSON != null){
            driver =  gson.fromJson(driverJSON, LoggedInDriver::class.java)
        }
    }

}