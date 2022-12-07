package com.example.fastugadriver

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.databinding.ActivityMainBinding
import com.example.fastugadriver.ui.*
import com.example.fastugadriver.ui.login.LoginActivity
import com.example.fastugadriver.websockets.SocketIOManager


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initiate LoginRepository
        LoginRepository.init(this)

        // initiate socket io
        SocketIOManager(this)

        if (!LoginRepository.isLoggedIn){
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        // Set default fragment
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            binding.bottomNavigationView.setVisibility(View.GONE);
            replaceFragment(LocationAuthFragment())
        }else{
            replaceFragment(MapsFragment())
        }

        binding.bottomNavigationView.selectedItemId = R.id.bottom_navbar_map

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId){
                R.id.bottom_navbar_map -> replaceFragment(MapsFragment())
                R.id.bottom_navbar_orders -> replaceFragment(OrdersFragment())
                R.id.bottom_navbar_profile -> replaceFragment(ProfileFragment())
            }
            true

        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    fun btnClick(view:View){
        if (LoginRepository.selectedOrder != null){
            val intent = Intent(this,  SelectedOrderDetailsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}