package com.example.fastugadriver.data.api

import com.example.fastugadriver.data.model.LoggedInDriver
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FasTugaLoginSuccessResponse : FasTugaResponse {
    @SerializedName("driver")
    @Expose
    val driver: LoggedInDriver? = null

    @SerializedName("token")
    @Expose
    val token: String? = null
}