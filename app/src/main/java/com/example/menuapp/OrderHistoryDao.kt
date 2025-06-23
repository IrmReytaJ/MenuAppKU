package com.example.menuapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderHistoryDao {
    @Insert
    suspend fun insertOrder(order: OrderHistory)

    @Delete
    suspend fun deleteOrder(order: OrderHistory)

    @Query("SELECT * FROM OrderHistory ORDER BY timestamp DESC")
    fun getAllOrders(): LiveData<List<OrderHistory>>
}
