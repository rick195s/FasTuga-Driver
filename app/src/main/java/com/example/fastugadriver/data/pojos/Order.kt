package com.example.fastugadriver.data.pojos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Order: FasTugaResponse {
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("ticket_number")
    @Expose
    val ticket_number: Int? = null

    @SerializedName("status")
    @Expose
    val status: String? = null

    @SerializedName("customer_id")
    @Expose
    val customer_id: Int? = null

    @SerializedName("total_price")
    @Expose
    val total_price: String? = null

    @SerializedName("total_paid")
    @Expose
    val total_paid: String? = null

    @SerializedName("total_paid_with_points")
    @Expose
    val total_paid_with_points: String? = null

    @SerializedName("points_gained")
    @Expose
    val points_gained: Int? = null

    @SerializedName("points_used_to_pay")
    @Expose
    val points_used_to_pay: Int? = null

    @SerializedName("payment_type")
    @Expose
    val payment_type: String? = null

    @SerializedName("payment_reference")
    @Expose
    val payment_reference: String? = null

    @SerializedName("date")
    @Expose
    val date: String? = null

    @SerializedName("delivered_by")
    @Expose
    val delivered_by: Int? = null

    @SerializedName("custom")
    @Expose
    val custom: String? = null

    @SerializedName("created_at")
    @Expose
    val created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    val updated_at: String? = null
}