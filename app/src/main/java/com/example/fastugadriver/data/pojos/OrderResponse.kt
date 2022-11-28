package com.example.fastugadriver.data.pojos

import com.example.fastugadriver.ui.OrdersFragment
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.Date

class OrderResponse : FasTugaResponse {

    @SerializedName("current_page")
    @Expose
    val current_page: Int? = null

    @SerializedName("data")
    @Expose
    val data: Array<Order>? = null

    @SerializedName("first_page_url")
    @Expose
    val first_page_url: String? = null

    @SerializedName("from")
    @Expose
    val from: Int? = null

    @SerializedName("last_page")
    @Expose
    val last_page: Int? = null

    @SerializedName("last_page_url")
    @Expose
    val last_page_url: String? = null

    @SerializedName("links")
    @Expose
    val links: Array<LinksPaginate>? = null
}