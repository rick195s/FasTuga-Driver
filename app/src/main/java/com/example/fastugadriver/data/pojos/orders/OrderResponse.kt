package com.example.fastugadriver.data.pojos.orders

import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.example.fastugadriver.data.pojos.paginate.Links
import com.example.fastugadriver.data.pojos.paginate.Meta
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrderResponse : FasTugaResponse {

    @SerializedName("data")
    @Expose
    val data: Array<Order>? = null

    @SerializedName("links")
    @Expose
    val links: Links? = null

    @SerializedName("meta")
    @Expose
    val meta: Meta? = null
    /*
    "data": [
    Data
    ],
    "links": {
        "first": "http://localhost/api/orders?page=1",
        "last": "http://localhost/api/orders?page=269",
        "prev": null,
        "next": "http://localhost/api/orders?page=2"
    },
    "meta": {
        Meta
        ],
        "path": "http://localhost/api/orders",
        "per_page": 10,
        "to": 10,
        "total": 2685
    */

}