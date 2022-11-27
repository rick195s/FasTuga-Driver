package com.example.fastugadriver.data.pojos.auth

import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Token : FasTugaResponse {

    @SerializedName("token_type")
    @Expose
     val tokenType: String? = null

    @SerializedName("expires_in")
    @Expose
     val expiresIn: Int? = null

    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null

    @SerializedName("refresh_token")
    @Expose
     val refreshToken: String? = null
}