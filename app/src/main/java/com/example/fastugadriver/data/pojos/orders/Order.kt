package com.example.fastugadriver.data.pojos.orders

import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Order: FasTugaResponse {
    @SerializedName("order_driver_delivery_id")
    @Expose
    val order_driver_delivery_id: Int? = null

    @SerializedName("delivery_location")
    @Expose
    val delivery_location: String? = null

    @SerializedName("tax_fee")
    @Expose
    val tax_fee: Int? = null

    @SerializedName("delivery_started_at")
    @Expose
    val delivery_started_at: String? = null

    @SerializedName("delivery_ended_at")
    @Expose
    val delivery_ended_at: String? = null

    @SerializedName("delivered_by")
    @Expose
    val delivered_by: Int? = null

    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("payment_type")
    @Expose
    val payment_type: String? = null

    @SerializedName("ticket_number")
    @Expose
    val ticket_number: Int? = null

   /* @SerializedName("products")
    @Expose
    val products: Array<Product>? = null*/
/*
    "order_driver_delivery_id": 1,
    "delivery_location": "Av. São. Carolina 9980 Almada",
    "tax_fee": 5,
    "delivery_started_at": "2022-09-20 00:07:58",
    "delivery_ended_at": "2022-09-20 00:11:29",
    "delivered_by": 24,
    "id": 9,
    "payment_type": "MBWAY"*/
}