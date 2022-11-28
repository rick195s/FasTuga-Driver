package com.example.fastugadriver.data.pojos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LinksPaginate : FasTugaResponse {
    @SerializedName("url")
    @Expose
    val url: String? = null

    @SerializedName("label")
    @Expose
    val label: String? = null

    @SerializedName("active")
    @Expose
    val active: Boolean? = null
}