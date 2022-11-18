package com.example.fastugadriver.ui.auth.register

import com.example.fastugadriver.data.model.LoggedInDriver

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: LoggedInDriver? = null,
    val error: RegisterErrors? = null
)