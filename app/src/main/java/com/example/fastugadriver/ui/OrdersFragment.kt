package com.example.fastugadriver.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.R
import com.example.fastugadriver.data.pojos.*
import com.example.fastugadriver.data.pojos.orders.OrderResponse
import com.example.fastugadriver.databinding.FragmentOrdersBinding
import com.example.fastugadriver.gateway.OrderGateway
import com.example.fastugadriver.ui.list.CustomAdapter
import com.example.fastugadriver.ui.list.ItemsViewModel

class OrdersFragment : Fragment() {
    private lateinit var adapter : CustomAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var data : ArrayList<ItemsViewModel>

    lateinit var text : Array<String>

    val orderGateway : OrderGateway = OrderGateway()
    private lateinit var binding: FragmentOrdersBinding
    val ownerFragment: LifecycleOwner = this@OrdersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOrdersBinding.inflate(layoutInflater)

        orderGateway.getOrders()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderGateway.fasTugaResponse.observe(ownerFragment, Observer {
            val orderResponse = it ?: return@Observer

            // handling API response
            when (orderResponse){
                is FormErrorResponse -> {
                    println("- is not possible to get Orders.")
                }

                is OrderResponse -> {
                    data = arrayListOf<ItemsViewModel>()
                    orderResponse.data?.forEach {
                        val item = ItemsViewModel(R.drawable.ic_baseline_add_box_24, it)
                        data.add(item)
                    }
                    val layoutManager = LinearLayoutManager(context)
                    recycleView = view.findViewById(R.id.orders_list)
                    recycleView.layoutManager = layoutManager
                    recycleView.setHasFixedSize(true)
                    adapter = CustomAdapter(data)
                    recycleView.adapter = adapter
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }
}