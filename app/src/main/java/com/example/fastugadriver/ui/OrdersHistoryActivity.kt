package com.example.fastugadriver.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.orders.OrderResponse
import com.example.fastugadriver.databinding.ActivityOrdersHistoryBinding
import com.example.fastugadriver.gateway.DriverGateway
import com.example.fastugadriver.ui.list.ItemsViewModel
import com.example.fastugadriver.ui.list.OrdersHistoryAdapter

class OrdersHistoryActivity : AppCompatActivity() {

    private lateinit var adapter : OrdersHistoryAdapter

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOrdersHistoryBinding
    private lateinit var data : ArrayList<ItemsViewModel>
    private lateinit var recycleView: RecyclerView

    val driverGateway : DriverGateway = DriverGateway()
    lateinit var orderResponse: OrderResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrdersHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        driverGateway.fasTugaResponse.observe(this@OrdersHistoryActivity, Observer {
            val orderResponse = it ?: return@Observer

            // handling API response
            when (orderResponse){
                is FormErrorResponse -> {
                    println("- is not possible to get Orders.")
                }

                is OrderResponse -> {
                    data = arrayListOf()
                    orderResponse.data?.forEach {
                        val item = ItemsViewModel(R.drawable.ic_baseline_add_box_24, it)
                        data.add(item)
                    }
                    val layoutManager = LinearLayoutManager(this)
                    binding.historyOrdersList.layoutManager = layoutManager
                    binding.historyOrdersList.setHasFixedSize(true)

                    binding.historyOrdersList.adapter = OrdersHistoryAdapter(data)
                    binding.historyOrdersPageIndex.text = orderResponse.meta?.current_page.toString()

                    this.orderResponse = orderResponse
                }
            }
        })

        driverGateway.getDriverOrders(LoginRepository.driver?.driverId)

        binding.historyOrdersNextBtn.setOnClickListener {

            orderResponse.meta?.last_page.let {
                if (it != null && orderResponse.meta?.current_page?.compareTo(it)!! < 0) {
                    driverGateway.getDriverOrders(LoginRepository.driver?.driverId, orderResponse.meta?.current_page?.plus(1))
                }
            }
        }

        binding.historyOrdersPrevBtn.setOnClickListener {
            if(orderResponse.meta?.current_page!! > 1){
                driverGateway.getDriverOrders(LoginRepository.driver?.driverId, orderResponse.meta?.current_page?.minus(1))
            }
        }
    }

}