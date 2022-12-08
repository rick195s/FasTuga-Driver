package com.example.fastugadriver.ui.list
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.R

class OrdersHistoryAdapter(private val mList: ArrayList<ItemsViewModel>) : RecyclerView.Adapter<OrdersHistoryAdapter.ViewHolder>() {

    //var selectedItem: View? = null
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_ordes_history_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        holder.taxView.text = "Tax Fee: " + ItemsViewModel.order.tax_fee
        holder.textViewTicketNumber.text = "Ticket Number: "+ItemsViewModel.order.ticket_number
        holder.textViewDeliveryLocation.text = ItemsViewModel.order.delivery_location
        holder.textViewDeliveryEndAt.text =  ItemsViewModel.order.delivery_ended_at
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val textViewTicketNumber: TextView = itemView.findViewById(R.id.textViewTicketNumber)
        val taxView: TextView = itemView.findViewById(R.id.taxView)
        val textViewDeliveryEndAt: TextView = itemView.findViewById(R.id.textViewDeliveryEndAt)
        val textViewDeliveryLocation: TextView = itemView.findViewById(R.id.TextViewDeliveryLocation)
    }
}