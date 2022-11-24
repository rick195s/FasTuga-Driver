package com.example.fastugadriver.data

import com.example.fastugadriver.data.pojos.LoggedInDriver

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

object LoginRepository {

    fun  login(driver: LoggedInDriver, token: String)  {
        setLoggedInDriver(driver)
        setToken(token)
    }

    // in-memory cache of the loggedInUser object
    var driver: LoggedInDriver? = null
        private set

    var token: String = ""
        private set

    val isLoggedIn: Boolean
        get() = driver != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        driver = null
    }

    fun logout() {
        driver = null
    }



    private fun setLoggedInDriver(loggedInDriver: LoggedInDriver) {
        this.driver = loggedInDriver
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    private fun setToken(token: String){
        this.token = "Bearer $token"
    }
}