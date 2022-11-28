package com.example.fastugadriver.data.pojos.paginate

import com.example.fastugadriver.data.pojos.FasTugaResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meta : FasTugaResponse {
    @SerializedName("current_page")
    @Expose
    val current_page: Int? = null

    @SerializedName("from")
    @Expose
    val from: Int? = null

    @SerializedName("last_page")
    @Expose
    val last_page: Int? = null

    @SerializedName("links")
    @Expose
    val links: Array<LinksPaginate>? = null

    @SerializedName("path")
    @Expose
    val path: String? = null

    @SerializedName("per_page")
    @Expose
    val per_page: Int? = null

    @SerializedName("to")
    @Expose
    val to: Int? = null

    @SerializedName("total")
    @Expose
    val total: Int? = null
}

/*
{
        "current_page": 1,
        "from": 1,
        "last_page": 269,
        "links": [
            {
                "url": null,
                "label": "&laquo; Previous",
                "active": false
            },
            {
                "url": "http://localhost/api/orders?page=1",
                "label": "1",
                "active": true
            }
        ],
        "path": "http://localhost/api/orders",
        "per_page": 10,
        "to": 10,
        "total": 2685
    }*/