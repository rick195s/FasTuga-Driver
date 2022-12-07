package com.example.fastugadriver.ui.list


import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fastugadriver.R
import com.example.fastugadriver.data.pojos.NotificationStored
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.coroutineContext


class NotificationsHistoryAdapter(private val mList: ArrayList<NotificationStored>) : RecyclerView.Adapter<NotificationsHistoryAdapter.NotificationViewHolder>() {

    //var selectedItem: View? = null
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_notifications_history_view_design, parent, false)

        return NotificationViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        val notification = mList[position]

        holder.textViewNotificationTicketNumber.text = "Ticket Number: " + notification.ticketNumber
        holder.textViewNotificationLocation.text = notification.location

        holder.textViewNotificationIcon.setImageURI(Uri.parse("@drawable/account_circle"))

        val cancelOrderDrawable: Drawable? =  AppCompatResources.getDrawable(holder.textViewNotificationIcon.context, R.drawable.ic_baseline_cancel_24)
        val orderReadyDrawable: Drawable? =  AppCompatResources.getDrawable(holder.textViewNotificationIcon.context, R.drawable.ic_baseline_delivery_dining_24)


        if (notification.type?.compareTo("order_cancelled") == 0){
            holder.textViewNotificationType.text = "Order Cancelled"
            holder.textViewNotificationIcon.setImageDrawable(cancelOrderDrawable)

        }else if(notification.type?.compareTo("order_ready")==0){
            holder.textViewNotificationType.text = "Order Ready"
            holder.textViewNotificationIcon.setImageDrawable(orderReadyDrawable)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class NotificationViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewNotificationLocation: TextView = itemView.findViewById(R.id.notification_location)
        val textViewNotificationTicketNumber: TextView = itemView.findViewById(R.id.notification_ticket_number)
        val textViewNotificationIcon: ImageView = itemView.findViewById(R.id.notification_icon)
        val textViewNotificationType: TextView = itemView.findViewById(R.id.notification_type)
    }
}