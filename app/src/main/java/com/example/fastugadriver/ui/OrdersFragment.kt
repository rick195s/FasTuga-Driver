package com.example.fastugadriver.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.R
import com.example.fastugadriver.data.pojos.*
import com.example.fastugadriver.databinding.FragmentOrdersBinding
import com.example.fastugadriver.gateway.OrderGateway
import com.example.fastugadriver.ui.list.CustomAdapter
import com.example.fastugadriver.ui.list.ItemsViewModel

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


    private lateinit var adapter : CustomAdapter
    private lateinit var recycleView: RecyclerView
    private lateinit var data : ArrayList<ItemsViewModel>

    lateinit var image : Array<Int>
    lateinit var text : Array<String>

    val orderGateway : OrderGateway = OrderGateway()
    private lateinit var binding: FragmentOrdersBinding
    val ownerFragment: LifecycleOwner = this@OrdersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOrdersBinding.inflate(layoutInflater)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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
                        val item = ItemsViewModel(R.drawable.ic_baseline_add_box_24, " Id: "+it.id.toString(),"Payment type: "+it.payment_type)
                        data.add(item)
                    }
                    val layoutManager = LinearLayoutManager(context)
                    recycleView = view.findViewById(R.id.ordersList)
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