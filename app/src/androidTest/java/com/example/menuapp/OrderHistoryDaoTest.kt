package com.example.menuapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OrderHistoryDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: MenuDatabase
    private lateinit var orderHistoryDao: OrderHistoryDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MenuDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        orderHistoryDao = db.orderHistoryDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertOrder_shouldAddOrderToDatabase() = runBlocking {
        val order = OrderHistory(
            itemsSummary = "2x Nasi Goreng",
            totalPrice = 30000,
            timestamp = System.currentTimeMillis()
        )
        orderHistoryDao.insertOrder(order)

        val orders = orderHistoryDao.getAllOrders().getOrAwaitValue()
        assertTrue(orders.any { it.itemsSummary.contains("Nasi Goreng") })
    }
}
