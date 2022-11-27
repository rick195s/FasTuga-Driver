package com.example.fastugadriver.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.auth.LogoutSuccessResponse
import com.example.fastugadriver.databinding.FragmentProfileBinding
import com.example.fastugadriver.gateway.DriverGateway
import com.example.fastugadriver.ui.login.LoginActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root


        val nameDetail = binding.profileDetailName
        val emailDetail = binding.profileDetailEmail
        val phoneDetail = binding.profileDetailsPhone
        val licensePlateDetail = binding.profileDetailLicensePlate
        val logoutButton = binding.profileButtonLogout

        nameDetail.text = LoginRepository.driver?.name ?: ""
        emailDetail.text = LoginRepository.driver?.email ?: ""
        phoneDetail.text = LoginRepository.driver?.phone ?: ""
        licensePlateDetail.text = LoginRepository.driver?.licensePlate ?: ""

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

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}