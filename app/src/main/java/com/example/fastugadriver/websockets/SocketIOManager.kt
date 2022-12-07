package com.example.fastugadriver.websockets

import android.content.Context
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.orders.Order
import com.example.fastugadriver.notifications.NotificationsManager
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class SocketIOManager(private val context: Context) {

    private var mSocket: Socket? = null

    private val onSocketConnectError = Emitter.Listener { args: Array<Any> ->
        println("erro")
    }

    private val onSocketConnect = Emitter.Listener { args: Array<Any> ->
        mSocket?.emit("register", "driver")

    }

    private val onOrderCancelled = Emitter.Listener { args: Array<Any> ->

        val  order: Order = Gson().fromJson(args[0].toString(), Order::class.java)
        val notificationManager = NotificationsManager(context)

        if (order.id == LoginRepository.selectedOrder?.id){
            notificationManager.orderCancelledNotification(order)
            LoginRepository.setOrder(null)
        }
    }

    private val onOrderReady = Emitter.Listener { args: Array<Any> ->
        val notificationManager = NotificationsManager(context)
        if (args[0].toString() == LoginRepository.selectedOrder?.id.toString()){
            LoginRepository.selectedOrder?.let { notificationManager.orderReadyNotification(it) }
        }
    }

    init{
        try {
            mSocket = IO.socket("http://10.0.2.2:8080")
        } catch ( e: URISyntaxException) {
        }

        mSocket?.on(Socket.EVENT_CONNECT_ERROR, onSocketConnectError);
        mSocket?.on(Socket.EVENT_CONNECT, onSocketConnect);
        mSocket?.on("order-cancelled",onOrderCancelled)
        mSocket?.on("order-ready", onOrderReady)
        mSocket?.connect();
    }
}