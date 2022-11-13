package com.example.fastugadriver.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fastugadriver.MainActivity
import com.example.fastugadriver.data.api.FasTugaFormErrorResponse
import com.example.fastugadriver.data.api.FasTugaResponse
import com.example.fastugadriver.data.api.FasTugaSuccessResponse

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
        val passwordConfirmation = binding.passwordConfirmation
        val phone = binding.phone
        val licensePlate = binding.licensePlate
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

            loading.visibility = View.GONE

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
            }else if (fasTugaResponse is FasTugaSuccessResponse){
                val intent = Intent(this, MainActivity::class.java)
                //intent.putExtra("key", value)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }


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
                    phone.text.toString(), licensePlate.text.toString(), password.text.toString(), passwordConfirmation.text.toString()));

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
                if (registerState.licensePlateError != null && !errorsList.contains(registerState.licensePlateError)) {
                    errorsList.add(registerState.licensePlateError)
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
                email.text.toString(),
                password.text.toString(),
                phone.text.toString(),
                licensePlate.text.toString()
            )


        }
    }
}