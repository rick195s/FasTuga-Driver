package com.example.fastugadriver.data

import com.example.fastugadriver.data.pojos.LoggedInDriver
import com.example.fastugadriver.data.pojos.Token

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

object LoginRepository {

    // in-memory cache of the loggedInUser object
    var driver: LoggedInDriver? = null

    var token: Token? = null
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

    fun setToken(newToken: Token){
        newToken.accessToken = "Bearer ${newToken.accessToken}"
        token = newToken
    }

}