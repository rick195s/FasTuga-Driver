package com.example.fastugadriver.ui.edit_profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.Driver
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.SuccessResponse
import com.example.fastugadriver.databinding.ActivityEditProfileBinding
import com.example.fastugadriver.gateway.DriverGateway
import java.io.File
import java.util.*


class EditProfileActivity : AppCompatActivity() {
    private lateinit var editProfileViewModel: EditProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding

    private val errorsList: LinkedList<Int> = LinkedList()

    private var resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // There are no request codes
            //newPhoto = FileUtils.(this, fileUri);

            val uri: Uri = result.data?.data as Uri
            newPhoto = createFileFromUri("photo", uri)
            Glide
                .with(this)
                .load(newPhoto)
                .circleCrop()
                .placeholder(R.drawable.account_circle)
                .into(binding.editProfileSelectedPhoto);
        }
    }


    private var newPhoto: File? = null

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
            }else{
                driverGateway.updateDriver(LoginRepository.driver?.driverId, preparedDriver(), newPhoto)
            }
        })

        driverGateway.fasTugaResponse.observe(this@EditProfileActivity, Observer {
            val fasTugaResponse = it ?: return@Observer

            // handling API response
            when (fasTugaResponse){
                is FormErrorResponse -> {
                    val errors : LinkedList<String> = LinkedList()
                    fasTugaResponse.errors?.oldPassword?.let { it1 -> errors.addAll(it1) }
                    fasTugaResponse.errors?.password?.let { it1 -> errors.addAll(it1) }
                    fasTugaResponse.errors?.phone?.let { it1 -> errors.addAll(it1) }
                    fasTugaResponse.errors?.licensePlate?.let { it1 -> errors.addAll(it1) }

                    fasTugaResponse.message?.let { it1 -> if(!errors.contains(it1)) errors.add(it1) }

                    showEditErrors(errors = errors)
                }

                is SuccessResponse -> {
                    onBackPressed()
                    finish()
                }
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

        binding.editProfileSelectPhotoButton.setOnClickListener {
            selectPhotoFromGallery()
        }

    }


    private fun selectPhotoFromGallery() {


        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
        resultLauncher.launch(chooserIntent)
    }

    private fun preparedDriver(): Driver {
        val driver : Driver = Driver()

        if (binding.editProfileNewPassword.text.isNotEmpty()){
            driver.old_password = binding.editProfileOldPassword.text.toString()
            driver.password = binding.editProfileNewPassword.text.toString()
            driver.password_confirmation = binding.editProfileNewPasswordConfirmation.text.toString()
        }


        if (binding.editProfilePhone.toString().compareTo(LoginRepository.driver?.phone.toString()) != 0) {
            driver.phone = binding.editProfilePhone.text.toString()
        }

        if (binding.editProfileLicensePlate.toString().compareTo(LoginRepository.driver?.licensePlate.toString()) != 0) {
            driver.license_plate = binding.editProfileLicensePlate.text.toString()
        }

        return driver
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

    private fun showEditErrors(editProfileErrors: EditProfileErrors? = null, errors: List<String>? = null) {
        errorsList.clear()
        binding.editProfileErrorMsgs.text = ""

        editProfileErrors?.phoneError?.let { errorsList.add(it) }
        editProfileErrors?.newPasswordError?.let { errorsList.add(it) }
        editProfileErrors?.newPasswordConfirmationError?.let { errorsList.add(it) }
        editProfileErrors?.licensePlateError?.let { errorsList.add(it) }

        for (error in errorsList) {
            binding.editProfileErrorMsgs.text = "${binding.editProfileErrorMsgs.text.toString()} - ${getString(error)}\n"
        }

        if (errors != null){
            for (error in errors){
                binding.editProfileErrorMsgs.text = "${binding.editProfileErrorMsgs.text.toString()} - ${error}\n"
            }
        }
    }


    private fun createFileFromUri(name: String, uri: Uri): File? {
        return try {
            val stream = contentResolver.openInputStream(uri)
            val file =
                File.createTempFile(
                    "${name}_${System.currentTimeMillis()}",
                    ".png",
                    cacheDir
                )

            stream?.copyTo(file.outputStream())
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}