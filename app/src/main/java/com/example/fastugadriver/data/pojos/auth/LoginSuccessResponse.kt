package com.example.fastugadriver.data.pojos.auth

import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.example.fastugadriver.data.pojos.auth.Token
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginSuccessResponse(driver: LoggedInDriver? = null, token: Token?= null) : FasTugaResponse {
    @SerializedName("driver")
    @Expose
    val driver: LoggedInDriver? = null

    @SerializedName("token")
    @Expose
    val token: Token? = null
}