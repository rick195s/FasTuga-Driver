package com.example.fastugadriver.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    lateinit var orderResponse: OrderResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderGateway.getOrders("All",1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ordersNextBtn.setOnClickListener {

            orderResponse.meta?.last_page.let {
            if (it != null && orderResponse.meta?.current_page?.compareTo(it)!! < 0) {
                    orderGateway.getOrders("All",orderResponse.meta?.current_page?.plus(1))
                }
            }

        }

        setList(orderGateway)

        binding.ordersPrevBtn.setOnClickListener {
            if(orderResponse.meta?.current_page!! > 1){
                orderGateway.getOrders("All", orderResponse.meta?.current_page?.minus(1))
            }
        }
    }

    fun setList(orderGateway: OrderGateway){
        orderGateway.fasTugaResponse.observe(ownerFragment, Observer {
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
                    val layoutManager = LinearLayoutManager(context)
                    recycleView = binding.root.findViewById(R.id.orders_list)
                    recycleView.layoutManager = layoutManager
                    recycleView.setHasFixedSize(true)
                    adapter = CustomAdapter(data)
                    recycleView.adapter = adapter
                    binding.ordersPageIndex.text = orderResponse.meta?.current_page.toString()

                    this.orderResponse = orderResponse
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val spinner = binding.filters
        val arraySpinner = arrayOf(
            "All","max.5km", "max.10km", "max.15km"," "
        )
        val arrayValues = arrayOf(
            "All","5", "10", "15","All"
        )
        var adapter = ArrayAdapter<String> (layoutInflater.context, android.R.layout.simple_spinner_item, arraySpinner)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                orderGateway.getOrders("All",1)
                setList(orderGateway)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                orderGateway.getOrders(arrayValues.get(spinner.selectedItemPosition),1)
                setList(orderGateway)
            }

        }
        return binding.root
    }
}