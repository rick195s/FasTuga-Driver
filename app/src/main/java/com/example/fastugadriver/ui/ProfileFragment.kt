package com.example.fastugadriver.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.databinding.FragmentProfileBinding

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


        nameDetail.text = LoginRepository.driver?.name ?: ""
        emailDetail.text = LoginRepository.driver?.email ?: ""
        phoneDetail.text = LoginRepository.driver?.phone ?: ""
        licensePlateDetail.text = LoginRepository.driver?.licensePlate ?: ""

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}