package com.example.fastugadriver.ui.register

 class RegisterErrors (
    var emailError: Int? = null,
    var passwordError: Int? = null,
    var passwordConfirmationError: Int? = null,
    var phoneError: Int? = null,
    var licensePlateError: Int? = null
) {
    fun hasErrors(): Boolean {
        return emailError != null || passwordError != null || passwordConfirmationError != null ||
                phoneError != null || licensePlateError != null
    }
}
