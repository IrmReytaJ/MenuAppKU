package com.example.menuapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val itemsSummary: String,
    val totalPrice: Int
)

