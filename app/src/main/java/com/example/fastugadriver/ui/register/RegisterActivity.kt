package com.example.fastugadriver.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fastugadriver.data.api.FasTugaFormErrorResponse

import com.example.fastugadriver.data.model.Driver
import com.example.fastugadriver.databinding.ActivityRegisterBinding
import com.example.fastugadriver.gateway.FasTugaAPI
import com.example.fastugadriver.ui.login.LoginActivity
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = binding.name
        val email = binding.email
        val password = binding.password
        val phone = binding.phone
        val license_plate = binding.licensePlate
        val login = binding.login
        val loading = binding.loading
        val errorMSGs = binding.errorMsgs
        val register = binding.register

        val errorsList: LinkedList<Int> = LinkedList()

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        val fasTugaAPI : FasTugaAPI = FasTugaAPI()

        fasTugaAPI.fasTugaResponse.observe(this@RegisterActivity, Observer {
            val fasTugaResponse = it ?: return@Observer

            if (fasTugaResponse is FasTugaFormErrorResponse){
                if (fasTugaResponse.errors?.email != null){
                    for (error in fasTugaResponse.errors.email){
                        errorMSGs.text = errorMSGs.text.toString() + "- " + error + "\n"
                    }
                }

                if (fasTugaResponse.errors?.phone != null){
                    for (error in fasTugaResponse.errors.phone){
                        errorMSGs.text = errorMSGs.text.toString() + "- " + error + "\n"
                    }
                }

            }

            loading.visibility = View.GONE

        })


        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            errorMSGs.text = ""
            errorsList.clear()

            // when there are no primary errors in the fields we call the API
            // to check for uniqueness errors
            if (registerState.isDataValid) {
                //registerViewModel.register(email.text.toString(), password.text.toString())
                loading.visibility = View.VISIBLE

                fasTugaAPI.registerDriver(Driver(name.text.toString(), email.text.toString(),
                    phone.text.toString(), license_plate.text.toString()));

            }else{

                errorsList.clear()
                if (registerState.emailError != null && !errorsList.contains(registerState.emailError)) {
                    errorsList.add(registerState.emailError)
                }
                if (registerState.passwordError != null && !errorsList.contains(registerState.passwordError)) {
                    errorsList.add(registerState.passwordError)
                }
                if (registerState.phoneError != null && !errorsList.contains(registerState.phoneError)) {
                    errorsList.add(registerState.phoneError)
                }

                for (error in errorsList) {
                    errorMSGs.text = errorMSGs.text.toString() + "- " + getString(error) + "\n"
                }
            }
        })

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            //intent.putExtra("key", value)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        register.setOnClickListener {
            // verify if fields are valid
            registerViewModel.validateRegister(
                email = email.text.toString(),
                password = password.text.toString(),
                phone = phone.text.toString()
            )


        }
    }
}