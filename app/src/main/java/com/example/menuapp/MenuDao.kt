package com.example.menuapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MenuDao {
    @Insert
    suspend fun insertOrder(order: OrderHistory)

    @Query("SELECT * FROM OrderHistory ORDER BY timestamp DESC")
    fun getAllOrders(): LiveData<List<OrderHistory>>

    @Query("SELECT * FROM menu_items")
    fun getAllMenus(): LiveData<List<MenuItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: MenuItem)

    @Update
    suspend fun updateMenu(menu: MenuItem)

    @Delete
    suspend fun deleteMenu(menu: MenuItem)

    @Query("DELETE FROM menu_items")
    suspend fun deleteAll()


}
