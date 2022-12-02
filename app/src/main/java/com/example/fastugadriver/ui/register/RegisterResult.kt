package com.example.fastugadriver.ui.register

import com.example.fastugadriver.data.pojos.auth.LoggedInDriver

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: LoggedInDriver? = null,
    val errors: RegisterErrors? = null
)