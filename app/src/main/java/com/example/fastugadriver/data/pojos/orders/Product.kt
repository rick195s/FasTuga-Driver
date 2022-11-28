package com.example.fastugadriver.data.pojos.orders

import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Product : FasTugaResponse {
    @SerializedName("id")
    @Expose
    val id: Int? = null

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("type")
    @Expose
    val type: String? = null

    @SerializedName("description")
    @Expose
    val description: String? = null

    @SerializedName("photo_url")
    @Expose
    val photo_url: String? = null

    @SerializedName("price")
    @Expose
    val price: String? = null

    @SerializedName("custom")
    @Expose
    val custom: String? = null

    @SerializedName("created_at")
    @Expose
    val created_at: String? = null

    @SerializedName("updated_at")
    @Expose
    val updated_at: String? = null

    @SerializedName("deleted_at")
    @Expose
    val deleted_at: String? = null
    /*"id": 49,
    "name": "Sumo de Ananás",
    "type": "drink",
    "description": "Queen will hear you! You see, she came upon a time she had quite forgotten the Duchess replied, in a shrill, loud voice, and see that the cause of this was his first speech. 'You should learn not to.",
    "photo_url": "TcVHyQ0ohjx2JRzq.jpg",
    "price": "2.00",
    "custom": null,
    "created_at": "2021-03-28T19:12:37.000000Z",
    "updated_at": "2021-09-14T15:27:49.000000Z",
    "deleted_at": null*/
}