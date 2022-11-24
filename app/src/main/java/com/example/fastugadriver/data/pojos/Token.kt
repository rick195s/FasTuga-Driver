package com.example.fastugadriver.data.pojos

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class Token {

    @SerializedName("token_type")
    @Expose
    private val tokenType: String? = null

    @SerializedName("expires_in")
    @Expose
    private val expiresIn: Int? = null

    @SerializedName("access_token")
    @Expose
    private val accessToken: String? = null

    @SerializedName("refresh_token")
    @Expose
    private val refreshToken: String? = null
}