package com.example.fastugadriver.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.Statistics
import com.example.fastugadriver.databinding.ActivityStatisticsBinding
import com.example.fastugadriver.gateway.StatisticsGateway

class StatisticsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding
    val statisticsGateway : StatisticsGateway = StatisticsGateway()
    lateinit var statistics: Statistics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statisticsGateway.fasTugaResponse.observe(this@StatisticsActivity, Observer {
            val statsResponse = it ?: return@Observer

            // handling API response
            when (statsResponse){
                is FormErrorResponse -> {
                    println("- is not possible to get Stats.")

                }

                is Statistics -> {
                    binding.statisticsConducted.append(" ${statsResponse.orders_delivered}")
                    binding.statisticsAvgTime.append(" ${statsResponse.average_time_to_deliver} minutes")
                    binding.statisticsTotalTime.append(" ${statsResponse.total_time_delivering} hours")
                    binding.statisticsDistinctCostumers.append(" ${statsResponse.distinct_costumers}")

                    this.statistics = statsResponse
                }
            }
        })

        statisticsGateway.getStats(LoginRepository.driver?.driverId)

    }

}