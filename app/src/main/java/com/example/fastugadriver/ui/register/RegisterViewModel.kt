package com.example.fastugadriver.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

import com.example.fastugadriver.R

class RegisterViewModel() : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun validateRegister(email: String, password: String, passwordConfirmation: String, phone: String, licensePlate: String){
        val registerErrors: RegisterErrors = RegisterErrors()

        if (!isEmailValid(email)) {
            registerErrors.emailError = R.string.invalid_email
        }

        if (!isPasswordValid(password)) {
            registerErrors.passwordError = R.string.invalid_password
        }

        if (!isPasswordConfirmationValid(password, passwordConfirmation)) {
            registerErrors.passwordConfirmationError = R.string.invalid_confirmation_password
        }

        if (!isValidPhone(phone)) {
            registerErrors.phoneError = R.string.invalid_phone
        }

        if (!isLicensePlateValid(licensePlate)) {
            registerErrors.licensePlateError = R.string.invalid_license_plate
        }

        if (registerErrors.hasErrors()){
            _registerResult.value = RegisterResult(errors = registerErrors)
        }else{
            _registerResult.value = RegisterResult(success = null )
        }
    }


    // email validation check
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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