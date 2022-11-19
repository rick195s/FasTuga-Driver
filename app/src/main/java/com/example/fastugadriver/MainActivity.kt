package com.example.fastugadriver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.databinding.ActivityMainBinding
import com.example.fastugadriver.databinding.ActivityRegisterBinding
import com.example.fastugadriver.ui.MapsFragment
import com.example.fastugadriver.ui.OrdersFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Maps Fragment as initial
        replaceFragment(MapsFragment())
        binding.bottomNavigationView.selectedItemId = R.id.bottom_navbar_map


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.bottom_navbar_orders -> replaceFragment(OrdersFragment())
                R.id.bottom_navbar_map -> replaceFragment(MapsFragment())

                else -> {

                }
            }
            true

        }

    }

    private fun replaceFragment(fragment: Fragment){
        val framentManager = supportFragmentManager
        val fragmentTransaction = framentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

}