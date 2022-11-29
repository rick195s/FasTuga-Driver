package com.example.fastugadriver.ui.edit_profile

 class EditProfileErrors (
    var newPasswordError: Int? = null,
    var newPasswordConfirmationError: Int? = null,
    var oldPasswordError: Int? = null,
    var photoError: Int? = null,
    var licensePlateError: Int? = null,
    var phoneError: Int? = null,
) {
    fun hasErrors(): Boolean {
        return newPasswordError != null || newPasswordConfirmationError != null || oldPasswordError != null || photoError != null
                || licensePlateError != null || phoneError != null
    }
}
