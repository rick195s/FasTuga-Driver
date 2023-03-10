package com.example.fastugadriver.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fastugadriver.MainActivity
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.auth.LoginSuccessResponse

import com.example.fastugadriver.data.pojos.Driver
import com.example.fastugadriver.databinding.ActivityLoginBinding
import com.example.fastugadriver.gateway.DriverGateway
import com.example.fastugadriver.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private var errorMSGs: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.loginEmail
        val password = binding.loginPassword
        val login = binding.login
        val loading = binding.loading
        val navigateRegister = binding.navigateRegister
        errorMSGs = binding.loginErrorMsgs


        val driverGateway : DriverGateway = DriverGateway()


        driverGateway.fasTugaResponse.observe(this@LoginActivity, Observer {
            val fasTugaResponse = it ?: return@Observer

            loading.visibility = View.GONE
            navigateRegister.isEnabled = true
            login.isEnabled = true

            // handling API response
            when (fasTugaResponse){
                is FormErrorResponse -> {
                    errorMSGs!!.text = "- Invalid Credentials."
                }

                is LoginSuccessResponse -> {
                    val intent = Intent(this, MainActivity::class.java)
                    //intent.putExtra("key", value)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                }
            }

        })

        login.setOnClickListener {
            navigateRegister.isEnabled = false
            login.isEnabled = false
            loading.visibility = View.VISIBLE

            login(email.text.toString(), password.text.toString(), driverGateway)

        }

        navigateRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            //intent.putExtra("key", value)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }


    private fun login(email: String, password: String, driverGateway: DriverGateway){

        try {
            driverGateway.loginDriver(Driver(email = email, password = password));

        }catch (e: Exception ){
            errorMSGs!!.text = "- ${e.message}"
        }

    }
}