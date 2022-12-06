package com.example.fastugadriver.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.orders.Order

class NotificationsManager (private val context: Context){

    private val channelId :String ="notification_channel"

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun orderCancelledNotification(order: Order) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_cancel_24)
            .setContentTitle("Order ${order.ticket_number} cancelled")
            .setContentText("Tax fee: ${order.tax_fee} €.\nLocation : ${order.delivery_location} ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (order.id == LoginRepository.selectedOrder?.id){
            notificationManager.notify(
                order.id!!, notification
            )
            LoginRepository.setOrder(null)
            saveNotification(notification)
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "FasTugaDriver Notifications"
            val descriptionText = "Used for notifications received from websockets"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun saveNotification(notification : Notification){

    }
}