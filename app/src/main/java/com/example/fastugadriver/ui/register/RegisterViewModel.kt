package com.example.fastugadriver.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.Result
import com.example.fastugadriver.ui.login.LoggedInUserView

import com.example.fastugadriver.R

class RegisterViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(email, password)

        if (result is Result.Success) {
            _registerResult.value =
                RegisterResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    fun validateRegister(email: String, password: String, passwordConfirmation: String, phone: String, licensePlate: String){
        var invalidEmail: Int? = null;
        var invalidPassword: Int? = null;
        var invalidPasswordConfirmation: Int? = null;
        var invalidPhone: Int? = null;
        var invalidLicensePlate: Int? = null;

        if (!isEmailValid(email)) {
            invalidEmail = R.string.invalid_email
        }

        if (!isPasswordValid(password)) {
            invalidPassword = R.string.invalid_password
        }

        if (!isPasswordConfirmationValid(password, passwordConfirmation)) {
            invalidPasswordConfirmation = R.string.invalid_confirmation_password
        }

        if (!isValidPhone(phone)) {
            invalidPhone = R.string.invalid_phone
        }

        if (!isLicensePlateValid(licensePlate)) {
            invalidLicensePlate = R.string.invalid_license_plate
        }


        _registerForm.value =  RegisterFormState(invalidEmail,invalidPassword,invalidPasswordConfirmation,invalidPhone, invalidLicensePlate)
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