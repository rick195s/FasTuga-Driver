package com.example.fastugadriver.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fastugadriver.MainActivity
import com.example.fastugadriver.data.api.FasTugaFormErrorResponse
import com.example.fastugadriver.data.api.FasTugaLoginSuccessResponse

import com.example.fastugadriver.data.model.Driver
import com.example.fastugadriver.databinding.ActivityRegisterBinding
import com.example.fastugadriver.gateway.FasTugaAPI
import com.example.fastugadriver.ui.auth.login.LoginActivity
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    private val errorsList: LinkedList<Int> = LinkedList()
    private var errorMSGs: TextView? = null


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
        val register = binding.register
        errorMSGs = binding.errorMsgs


        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        val fasTugaAPI : FasTugaAPI = FasTugaAPI()

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val registerResult = it ?: return@Observer

            errorMSGs!!.text = ""
            errorsList.clear()

            // when there are no primary errors in the fields we call the API
            // to check for uniqueness errors
            if (registerResult.error != null) {
                showRegisterErrors(registerResult.error)

            }else{
                loading.visibility = View.VISIBLE

                try {
                    fasTugaAPI.registerDriver(Driver(name.text.toString(), email.text.toString(),
                        phone.text.toString(), licensePlate.text.toString(), password.text.toString(), passwordConfirmation.text.toString()));

                }catch (e: Exception ){
                    val error : LinkedList<String> = LinkedList<String>()
                    error.add(e.message.toString())
                    showRegisterErrors( errors = error)
                }

            }
        })

        fasTugaAPI.fasTugaResponse.observe(this@RegisterActivity, Observer {
            val fasTugaResponse = it ?: return@Observer

            loading.visibility = View.GONE

            // handling API response
            when (fasTugaResponse){
                is FasTugaFormErrorResponse -> {
                    val errors : LinkedList<String> = LinkedList()
                    fasTugaResponse.errors?.email?.let { it1 -> errors.addAll(it1) }
                    fasTugaResponse.errors?.phone?.let { it1 -> errors.addAll(it1) }

                    showRegisterErrors(errors = errors)
                }

                is FasTugaLoginSuccessResponse -> {
                    registerViewModel.login(fasTugaResponse.driver!!, fasTugaResponse.token!!)

                    println()
                    val intent = Intent(this, MainActivity::class.java)
                    //intent.putExtra("key", value)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
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
                passwordConfirmation.text.toString(),
                phone.text.toString(),
                licensePlate.text.toString()
            )


        }
    }


    private fun showRegisterErrors(registerErrors: RegisterErrors? = null, errors:List<String>?= null) {
        errorsList.clear()
        if (registerErrors?.emailError != null && !errorsList.contains(registerErrors.emailError)) {
            errorsList.add(registerErrors.emailError!!)
        }
        if (registerErrors?.passwordError != null && !errorsList.contains(registerErrors.passwordError)) {
            errorsList.add(registerErrors.passwordError!!)
        }
        if (registerErrors?.passwordConfirmationError != null && !errorsList.contains(registerErrors.passwordConfirmationError)) {
            errorsList.add(registerErrors.passwordConfirmationError!!)
        }
        if (registerErrors?.phoneError != null && !errorsList.contains(registerErrors.phoneError)) {
            errorsList.add(registerErrors.phoneError!!)
        }
        if (registerErrors?.licensePlateError != null && !errorsList.contains(registerErrors.licensePlateError)) {
            errorsList.add(registerErrors.licensePlateError!!)
        }

        if (errors != null) {
            for (error in errors) {
                errorMSGs!!.text = errorMSGs!!.text.toString() + "- " + error + "\n"
            }
        }

        for (error in errorsList) {
            errorMSGs!!.text = errorMSGs!!.text.toString() + "- " + getString(error) + "\n"
        }
    }
}