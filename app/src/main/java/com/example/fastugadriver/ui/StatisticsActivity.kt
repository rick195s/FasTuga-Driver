package com.example.fastugadriver.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fastugadriver.R
import com.example.fastugadriver.databinding.ActivityStatisticsBinding

class StatisticsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_statistics)

    }

}