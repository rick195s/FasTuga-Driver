package com.example.fastugadriver.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.NotificationStored
import com.example.fastugadriver.data.pojos.orders.OrderResponse
import com.example.fastugadriver.databinding.ActivityNotificationsHistoryBinding
import com.example.fastugadriver.databinding.ActivityOrdersHistoryBinding
import com.example.fastugadriver.gateway.DriverGateway
import com.example.fastugadriver.notifications.NotificationsManager
import com.example.fastugadriver.ui.list.ItemsViewModel
import com.example.fastugadriver.ui.list.NotificationsHistoryAdapter
import com.example.fastugadriver.ui.list.OrdersHistoryAdapter

class NotificationsHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsHistoryBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_notifications_history)

        val layoutManager = LinearLayoutManager(this)
        binding.historyNotificationsList.layoutManager = layoutManager
        binding.historyNotificationsList.setHasFixedSize(true)
        val notificationManager =  NotificationsManager(this)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.history_notifications_list)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter
        val adapter = NotificationsHistoryAdapter(notificationManager.getNotifications())

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }
}