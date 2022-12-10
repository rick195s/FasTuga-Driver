package com.example.fastugadriver.data.pojos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Statistics : FasTugaResponse {
    @SerializedName("orders_delivered")
    @Expose
    var orders_delivered: Int? = null

    @SerializedName("average_time_to_deliver")
    @Expose
    var average_time_to_deliver: String? = null

    @SerializedName("total_time_delivering")
    @Expose
    var total_time_delivering: Int? = null

    @SerializedName("distinct_costumers")
    @Expose
    var distinct_costumers: String? = null

}