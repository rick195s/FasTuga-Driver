package com.example.fastugadriver.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.fastugadriver.MainActivity
import com.example.fastugadriver.R
import com.example.fastugadriver.data.pojos.*
import com.example.fastugadriver.gateway.DriverGateway
import com.example.fastugadriver.gateway.FasTugaAPI
import com.example.fastugadriver.gateway.OrderGateway
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrdersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrdersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val orderGateway : OrderGateway = OrderGateway()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        
        orderGateway.fasTugaResponse.observe(this@OrdersFragment, Observer {
            val orderResponse = it ?: return@Observer

            // handling API response
            when (orderResponse){
                is FormErrorResponse -> {
                    println("- is not possible to get Orders.")
                }

                is OrderResponse -> {
                    orderResponse.data?.forEach {
                        print("Id: ")
                        print(it.id)
                        print(" Payment type: ")
                        print(it.payment_type)
                        print(" Delivered by: ")
                        println(it.delivered_by)
                    }

                    //Feel list view
                }
            }

        })

        orderGateway.getOrders()

        /*try {
            println("ORDERS-> "+orderGateway.getOrders())
            println("--> "+orderGateway.fasTugaResponse.value)

        }catch (e: Exception ){
            println("ERROR")
        }*/


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrdersFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrdersFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}