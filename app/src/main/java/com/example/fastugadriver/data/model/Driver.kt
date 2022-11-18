package com.example.fastugadriver.data.model


data class Driver(
    val name: String? = null,
    val email: String,
    val phone: String? = null,
    val license_plate: String? = null ,
    val password: String,
    val password_confirmation: String? = null
)