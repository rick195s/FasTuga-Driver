package com.example.fastugadriver.ui.register

/**
 * Data validation state of the login form.
 */
data class RegisterFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val passwordConfirmationError: Int? = null,
    val phoneError: Int? = null,
    val licensePlateError: Int? = null
){

    val isDataValid: Boolean
        get(): Boolean {
            return emailError == null && passwordError == null && passwordConfirmationError == null && phoneError == null && licensePlateError == null
        }

}