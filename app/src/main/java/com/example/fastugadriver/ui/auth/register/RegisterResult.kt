package com.example.fastugadriver.ui.auth.register

import com.example.fastugadriver.ui.auth.login.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class RegisterResult(
    val success: LoggedInUserView? = null,
    val error: RegisterErrors? = null
)