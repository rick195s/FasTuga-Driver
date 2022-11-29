package com.example.fastugadriver.data.pojos

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

data class Driver(
    val name: String? = null,
    val email: String? = null,
    var phone: String? = null,
    var license_plate: String? = null,
    var password: String? = null,
    var password_confirmation: String? = null,
    var old_password: String? = null,
) {
    fun toMap() : Map<String, RequestBody> {
        val map: HashMap<String, RequestBody> = HashMap()

        if (name != null) map["name"] = name.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
        if (email != null) map["email"] = email.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
        if (phone != null) map["phone"] = phone!!.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
        if (license_plate != null) map["license_plate"] = license_plate!!.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
        if (password != null) map["password"] = password!!.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
        if (password_confirmation != null) map["password_confirmation"] = password_confirmation!!.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())
        if (old_password != null) map["old_password"] = old_password!!.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull())


        return map
    }
}