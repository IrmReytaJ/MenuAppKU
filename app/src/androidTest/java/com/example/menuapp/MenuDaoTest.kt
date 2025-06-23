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
class MenuDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: MenuDatabase
    private lateinit var dao: MenuDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MenuDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.menuDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertMenuItem_shouldReturnInsertedItem() = runBlocking {
        val menu = MenuItem(
            name = "Nasi Goreng",
            price = 15000,
            imageResId = 0,
            category = "Makanan"
        )
        dao.insertMenu(menu)

        val result = dao.getAllMenus().getOrAwaitValue()
        assertTrue(result.any { it.name == "Nasi Goreng" })
    }
}
