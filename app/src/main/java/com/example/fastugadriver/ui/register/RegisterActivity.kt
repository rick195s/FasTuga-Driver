package com.example.fastugadriver.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fastugadriver.MainActivity
import com.example.fastugadriver.data.pojos.FasTugaFormErrorResponse
import com.example.fastugadriver.data.pojos.FasTugaLoginSuccessResponse

import com.example.fastugadriver.data.pojos.Driver
import com.example.fastugadriver.databinding.ActivityRegisterBinding
import com.example.fastugadriver.gateway.DriverGateway
import com.example.fastugadriver.ui.login.LoginActivity
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

        val name = binding.registerName
        val email = binding.registerEmail
        val password = binding.registerPassword
        val passwordConfirmation = binding.registerPasswordConfirmation
        val phone = binding.registerPhone
        val licensePlate = binding.registerLicensePlate
        val login = binding.navigateLogin
        val loading = binding.loading
        val register = binding.register
        errorMSGs = binding.registerErrorMsgs


        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        val driverGateway : DriverGateway = DriverGateway()

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val registerResult = it ?: return@Observer

            errorMSGs!!.text = ""
            errorsList.clear()

            // when there are no primary errors in the fields we call the API
            // to check for uniqueness errors
            if (registerResult.error != null) {
                showRegisterErrors(registerResult.error)
                register.isEnabled = true
                login.isEnabled = true

            }else{
                loading.visibility = View.VISIBLE

                try {
                    driverGateway.registerDriver(Driver(name.text.toString(), email.text.toString(),
                        phone.text.toString(), licensePlate.text.toString(), password.text.toString(), passwordConfirmation.text.toString()));

                }catch (e: Exception ){
                    val error : LinkedList<String> = LinkedList<String>()
                    error.add(e.message.toString())
                    showRegisterErrors( errors = error)
                }



            }
        })

        driverGateway.fasTugaResponse.observe(this@RegisterActivity, Observer {
            val fasTugaResponse = it ?: return@Observer

            loading.visibility = View.GONE
            register.isEnabled = true
            login.isEnabled = true

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
            register.isEnabled = false
            login.isEnabled = false

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