package com.example.fastugadriver.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoggedInDriver {

    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

    @SerializedName("type")
    @Expose
    val type: String? = null

    @SerializedName("typeToString")
    @Expose
    val typeToString: String? = null

    @SerializedName("blocked")
    @Expose
    val blocked: Boolean? = null

    @SerializedName("photo_url")
    @Expose
    val photoUrl: String? = null

    @SerializedName("driver_id")
    @Expose
    val driverId: Int? = null

    @SerializedName("phone")
    @Expose
    val phone: String? = null

    @SerializedName("license_plate")
    @Expose
    val licensePlate: String? = null
}