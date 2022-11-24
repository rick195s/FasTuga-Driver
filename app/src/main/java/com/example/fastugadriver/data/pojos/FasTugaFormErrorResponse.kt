package com.example.fastugadriver.data.pojos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.LinkedList

class FasTugaFormErrorResponse : FasTugaResponse {
        @SerializedName("message")
        @Expose
        val message: String? = null

        @SerializedName("errors")
        @Expose
        val errors: Errors? = null
}