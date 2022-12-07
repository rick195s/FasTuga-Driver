package com.example.fastugadriver.data.pojos

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationStored
constructor(@SerializedName("type") @Expose val type: String?,
            @SerializedName("location") @Expose val location: String?,
            @SerializedName("ticket_number") @Expose val ticketNumber: String?
)