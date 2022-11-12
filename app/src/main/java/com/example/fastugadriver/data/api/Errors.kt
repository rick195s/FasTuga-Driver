package com.example.fastugadriver.data.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Errors {

    @SerializedName("name")
    @Expose
     val name: List<String>? = null

    @SerializedName("email")
    @Expose
     val email: List<String>? = null

    @SerializedName("password")
    @Expose
    val password: List<String>? = null

    @SerializedName("phone")
    @Expose
     val phone: List<String>? = null

    @SerializedName("license_plate")
    @Expose
     val licensePlate: List<String>? = null

}