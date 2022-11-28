package com.example.fastugadriver.ui.edit_profile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.fastugadriver.R
import com.example.fastugadriver.databinding.ActivityEditProfileBinding
import com.example.fastugadriver.gateway.DriverGateway
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.fastugadriver.data.LoginRepository
import java.net.URI
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding

    private val errorsList: LinkedList<Int> = LinkedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editProfileViewModel = ViewModelProvider(this,EditProfileViewModelFactory())
            .get(EditProfileViewModel::class.java)

        val driverGateway : DriverGateway = DriverGateway()

        setDefaultFields()

        editProfileViewModel.editProfileResult.observe(this@EditProfileActivity, Observer {
            val editProfileResult = it  ?: return@Observer

            if (editProfileResult.errors != null){
                showEditErrors(editProfileResult.errors)
            }
        })

        binding.editProfileUpdate.setOnClickListener {
            editProfileViewModel.validateEdit(
                binding.editProfileNewPassword.text.toString(),
                binding.editProfileNewPasswordConfirmation.text.toString(),
                binding.editProfilePhone.text.toString(),
                binding.editProfileLicensePlate.text.toString(),
            )
        }

    }

    private fun setDefaultFields() {
        binding.editProfilePhone.setText(LoginRepository.driver?.phone)
        binding.editProfileLicensePlate.setText(LoginRepository.driver?.licensePlate)

        if (LoginRepository.driver?.photoUrl != null){
            Glide
                .with(this)
                .load(LoginRepository.driver?.photoUrl)
                .circleCrop()
                .placeholder(R.drawable.account_circle)
                .into(binding.editProfileSelectedPhoto);
        }


    }

    private fun showEditErrors(editProfileErrors: EditProfileErrors) {
        errorsList.clear()
        binding.editProfileErrorMsgs.text = ""


        editProfileErrors.phoneError?.let { errorsList.add(it) }
        editProfileErrors.newPasswordError?.let { errorsList.add(it) }
        editProfileErrors.newPasswordConfirmationError?.let { errorsList.add(it) }
        editProfileErrors.licensePlateError?.let { errorsList.add(it) }

        for (error in errorsList) {
            binding.editProfileErrorMsgs.text = "${binding.editProfileErrorMsgs.text.toString()} - ${getString(error)}\n"
        }
    }
}