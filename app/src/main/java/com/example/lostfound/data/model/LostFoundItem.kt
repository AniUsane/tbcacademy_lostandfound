package com.example.lostfound.data.model

import java.util.Date

//data class for items
data class LostFoundItem(
    val id: String = "",
    val title: String = "Found wallet",
    val description: String = "Found wallet in the mall",
    val date: Date = Date(),
    val userId: String = "user123",
    val status: ItemStatus = ItemStatus.LOST
)
