package com.example.fastugadriver.ui.register

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null
){

    val isDataValid: Boolean
        get(): Boolean {
            return emailError == null && passwordError == null
        }

}