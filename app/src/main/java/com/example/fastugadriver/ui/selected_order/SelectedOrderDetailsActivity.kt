package com.example.fastugadriver.ui.selected_order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.fastugadriver.MainActivity
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.SuccessResponse
import com.example.fastugadriver.data.pojos.auth.LogoutSuccessResponse
import com.example.fastugadriver.data.pojos.orders.Order
import com.example.fastugadriver.databinding.ActivitySelectedOrderDetailsBinding
import com.example.fastugadriver.gateway.OrderGateway
import com.example.fastugadriver.ui.login.LoginActivity

class SelectedOrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectedOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_order_details)

        binding = ActivitySelectedOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var order : Order = LoginRepository.selectedOrder as Order

        binding.selectedOrderTicketNumber.text = order.ticket_number.toString()
        binding.selectedOrderCustomerLocation.text = order.delivery_location
        binding.selectedOrderTaxFee.text = order.tax_fee.toString()

        binding.selectedOrderDetails.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        val orderGateway = OrderGateway()
        orderGateway.fasTugaResponse.observe(this, Observer {
            val fasTugaResponse = it ?: return@Observer

            // handling API response
            when (fasTugaResponse){
                is SuccessResponse -> {
                    LoginRepository.setOrder(null)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                }
            }
        })

        binding.selectedOrderCancelOrderBtn.setOnClickListener {
            orderGateway.cancelOrder()
        }
    }
}