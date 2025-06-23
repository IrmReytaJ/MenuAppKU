package com.example.menuapp

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MenuViewModel(application: Application) : AndroidViewModel(application) {

    private val menuDao = MenuDatabase.getDatabase(application).menuDao()
    private val orderDao = MenuDatabase.getDatabase(application).orderHistoryDao()

    val menuList: LiveData<List<MenuItem>> = menuDao.getAllMenus()

    val orderHistoryList: LiveData<List<OrderHistory>> = orderDao.getAllOrders()

    fun insertMenu(menu: MenuItem) = viewModelScope.launch {
        menuDao.insertMenu(menu)
    }

    fun updateMenu(menu: MenuItem) = viewModelScope.launch {
        menuDao.updateMenu(menu)
    }

    fun deleteAllMenus() = viewModelScope.launch {
        menuDao.deleteAll()
    }

    fun insertOrder(order: OrderHistory) = viewModelScope.launch {
        orderDao.insertOrder(order)
    }

    fun deleteOrder(order: OrderHistory) = viewModelScope.launch {
        orderDao.deleteOrder(order)
    }
}
