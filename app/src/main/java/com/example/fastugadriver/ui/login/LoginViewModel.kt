package com.example.fastugadriver.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fastugadriver.data.LoginRepository

import com.example.fastugadriver.data.pojos.LoggedInDriver

class LoginViewModel() : ViewModel() {

    fun login(driver: LoggedInDriver, token: String) {
        val loginRepository: LoginRepository = LoginRepository
        loginRepository.login(driver, token)
    }

}