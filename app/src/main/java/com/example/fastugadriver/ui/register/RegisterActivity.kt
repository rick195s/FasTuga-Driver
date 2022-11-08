package com.example.fastugadriver.ui.register

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.fastugadriver.databinding.ActivityRegisterBinding

import com.example.fastugadriver.R
import java.util.LinkedList

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val errorMSGs = binding.errorMsgs;

        val errorsList: LinkedList<Int> = LinkedList()

        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory())
            .get(RegisterViewModel::class.java)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            errorsList.clear()
            if (registerState.emailError != null && !errorsList.contains(registerState.emailError)) {
                errorsList.add(registerState.emailError)
            }
            if (registerState.passwordError != null && !errorsList.contains(registerState.passwordError)) {
                errorsList.add(registerState.passwordError)
            }

            errorMSGs.text = ""
            for (error in errorsList) {
                errorMSGs.text = errorMSGs.text.toString() + "- " +getString(error) +"\n"
            }
        })

        registerViewModel.registerResult.observe(this@RegisterActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })


        login.setOnClickListener {
            registerViewModel.validateRegister(
                email = email.text.toString(),
                password = password.text.toString()
            )

            if (registerViewModel.registerFormState.value!!.isDataValid){
                registerViewModel.register(email.text.toString(), password.text.toString())
                loading.visibility = View.VISIBLE
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
