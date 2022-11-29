package com.example.fastugadriver.data.pojos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FormErrorResponse(message: String? = null) : FasTugaResponse {
        @SerializedName("message")
        @Expose
        val message: String? = message

        @SerializedName("errors")
        @Expose
        val errors: Errors? = null
}
