package com.example.fastugadriver.ui.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.fastugadriver.R

class EditProfileViewModel() : ViewModel() {
    private val _editProfileResult = MutableLiveData<EditProfileResult>()
    val editProfileResult: LiveData<EditProfileResult> = _editProfileResult

    fun validateRegister(email: String, newPassword: String, newPasswordConfirmation: String, phone: String, licensePlate: String){
        val editProfileErrors: EditProfileErrors = EditProfileErrors()

        if (!isPasswordValid(newPassword)) {
            editProfileErrors.newPasswordError = R.string.invalid_password
        }

        if (!isPasswordConfirmationValid(newPassword, newPasswordConfirmation)) {
            editProfileErrors.newPasswordConfirmationError = R.string.invalid_confirmation_password
        }

        if (!isValidPhone(phone)) {
            editProfileErrors.phoneError = R.string.invalid_phone
        }

        if (!isLicensePlateValid(licensePlate)) {
            editProfileErrors.licensePlateError = R.string.invalid_license_plate
        }

        if (editProfileErrors.hasErrors()){
            _editProfileResult.value = EditProfileResult(error = editProfileErrors)
        }else{
            _editProfileResult.value = EditProfileResult(success = null )
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        val pattern1 = Regex("(9[0-9])\\w{0,8}")
        return pattern1.matches(phone)
        //(9[0-9])\w{0,8}
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    private fun isLicensePlateValid(licensePlate: String): Boolean{
        return licensePlate.matches(Regex("[\\d\\w]{2}[-][\\d\\w]{2}[-][\\d\\w]{2}|[\\d\\w]{2}[ ][\\d\\w]{2}[ ][\\d\\w]{2}"))
    }

    private fun isPasswordConfirmationValid(password: String, passwordConfirmation: String): Boolean {
        return password.compareTo(passwordConfirmation) == 0
    }
}