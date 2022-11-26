package com.example.fastugadriver.data

import android.content.Context
import android.content.SharedPreferences
import com.example.fastugadriver.data.pojos.LoggedInDriver
import com.example.fastugadriver.data.pojos.Token
import com.google.gson.Gson

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
    }

    // in-memory cache of the loggedInUser object
    var driver: LoggedInDriver? = null
        private set

    var token: Token? = null
        private set

    val isLoggedIn: Boolean
        get() = driver != null && token != null

    fun logout() {
        driver = null
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