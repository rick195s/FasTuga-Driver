package com.example.fastugadriver.data.pojos


data class Driver(
    val name: String? = null,
    val email: String? = null,
    var phone: String? = null,
    var license_plate: String? = null,
    var password: String? = null,
    var password_confirmation: String? = null,
    var old_password: String? = null,
    val photoUrl: String? = null
)