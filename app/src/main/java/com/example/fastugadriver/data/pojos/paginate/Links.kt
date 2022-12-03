package com.example.fastugadriver.data.pojos.paginate

import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Links : FasTugaResponse {
    @SerializedName("first")
    @Expose
    val first: String? = null

    @SerializedName("last")
    @Expose
    val last: String? = null

    @SerializedName("prev")
    @Expose
    val prev: String? = null

    @SerializedName("next")
    @Expose
    val next: String? = null

    /*
        "first": "http://localhost/api/orders?page=1",
        "last": "http://localhost/api/orders?page=269",
        "prev": null,
        "next": "http://localhost/api/orders?page=2"
     */
}