package com.example.fastugadriver.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.auth.LogoutSuccessResponse
import com.example.fastugadriver.databinding.FragmentProfileBinding
import com.example.fastugadriver.gateway.DriverGateway
import com.example.fastugadriver.ui.edit_profile.EditProfileActivity
import com.example.fastugadriver.ui.login.LoginActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root


        val nameDetail = binding.profileDetailName
        val emailDetail = binding.profileDetailEmail
        val phoneDetail = binding.profileDetailsPhone
        val licensePlateDetail = binding.profileDetailLicensePlate
        val logoutButton = binding.profileButtonLogout
        val editButton  = binding.profileButtonEdit

        nameDetail.text = LoginRepository.driver?.name ?: ""
        emailDetail.text = LoginRepository.driver?.email ?: ""
        phoneDetail.text = LoginRepository.driver?.phone ?: ""
        licensePlateDetail.text = LoginRepository.driver?.licensePlate ?: ""

        Glide
            .with(this)
            .load(LoginRepository.driver?.photoUrl)
            .circleCrop()
            .placeholder(R.drawable.account_circle)
            .into(binding.profileUserImage)

        val driverGateway : DriverGateway = DriverGateway()

        driverGateway.fasTugaResponse.observe(viewLifecycleOwner, Observer {
            val fasTugaResponse = it ?: return@Observer

            // handling API response
            when (fasTugaResponse){
                is LogoutSuccessResponse -> {
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
            }

        })


        logoutButton.setOnClickListener {
            driverGateway.logoutDriver()
        }

        editButton.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.profileButtonHistory.setOnClickListener {
            val intent = Intent(activity, OrdersHistoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        binding.profileButtonNotifications.setOnClickListener {
            val intent = Intent(activity, NotificationsHistoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}