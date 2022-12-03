package com.example.fastugadriver.ui.list
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.R
import com.example.fastugadriver.data.LoginRepository

class CustomAdapter(private val mList: ArrayList<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //var selectedItem: View? = null
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        holder.itemView.setOnClickListener {

            LoginRepository.setOrder(ItemsViewModel.order)
            /*selectedItem?.setBackgroundColor(Color.WHITE)
            holder.itemView.setBackgroundColor(Color.parseColor("#8BC34A"))
            selectedItem = holder.itemView*/
        }
        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = "Id: "+ItemsViewModel.order.id

        holder.textViewTicketNumber.text = "Ticket Number: "+ItemsViewModel.order.ticket_number

        holder.textViewDeliveryLocation.text = "Location: "+ItemsViewModel.order.delivery_location

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageview)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val textViewTicketNumber: TextView = itemView.findViewById(R.id.textViewTicketNumber)
        val textViewDeliveryLocation: TextView = itemView.findViewById(R.id.TextViewDeliveryLocation)
    }
}