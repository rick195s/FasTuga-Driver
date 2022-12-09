package com.example.fastugadriver.ui.list
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.MainActivity
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository
import com.example.fastugadriver.data.pojos.FormErrorResponse
import com.example.fastugadriver.data.pojos.SuccessResponse
import com.example.fastugadriver.gateway.OrderGateway
import com.example.fastugadriver.ui.OrdersFragment

class OrderAdapter(private val mList: ArrayList<ItemsViewModel>, private val fragment : OrdersFragment) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }



    // binds the list items to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        val orderGateway = OrderGateway()
        orderGateway.fasTugaResponse.observe(fragment.viewLifecycleOwner, Observer {
            val orderResponse = it ?: return@Observer

            when (orderResponse) {
                is SuccessResponse -> {
                    LoginRepository.setOrder(ItemsViewModel.order)
                    //Start Main activity
                    val intent = Intent(fragment.context, MainActivity::class.java)
                    fragment.startActivity(intent)
                }
            }
        })

        holder.itemView.setOnClickListener {
            orderGateway.updateOrderDeliveredBy(ItemsViewModel.order.id)
        }
        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)
        holder.textViewTicketNumber.text = "Ticket Number: "+ItemsViewModel.order.ticket_number
        holder.textViewDeliveryLocation.text = "Location: "+ItemsViewModel.order.delivery_location
        holder.textViewDistance.text = ItemsViewModel.order.distance.toString() + "Km"
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)

        val textViewTicketNumber: TextView = itemView.findViewById(R.id.textViewTicketNumber)
        val textViewDeliveryLocation: TextView = itemView.findViewById(R.id.TextViewDeliveryLocation)
        val textViewDistance: TextView = itemView.findViewById(R.id.textViewDistance)
    }
}