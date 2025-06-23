package com.example.menuapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.menuapp.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MenuViewModel by viewModels()
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeMenus()
        setupSaveButton()
    }

    private fun setupRecyclerView() {
        menuAdapter = MenuAdapter { updatedItem ->
            viewModel.updateMenu(updatedItem)
            updateTotalPriceFromMenus()
        }

        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (menuAdapter.getItemViewType(position)) {
                    MenuAdapter.VIEW_TYPE_HEADER -> 2
                    MenuAdapter.VIEW_TYPE_MENU -> 1
                    else -> 1
                }
            }
        }

        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            adapter = menuAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeMenus() {
        viewModel.menuList.observe(this) { menus ->
            if (menus.isEmpty()) {
                seedInitialData()
            } else {
                updateMenuListWithHeaders(menus)
            }
        }
    }

    private fun updateMenuListWithHeaders(menus: List<MenuItem>) {
        val makanan = menus.filter { it.category == "Makanan" }
        val minuman = menus.filter { it.category == "Minuman" }

        val combined = mutableListOf<MenuListItem>()
        if (makanan.isNotEmpty()) {
            combined.add(MenuListItem.Header("Makanan"))
            combined.addAll(makanan.map { MenuListItem.Menu(it) })
        }

        if (minuman.isNotEmpty()) {
            combined.add(MenuListItem.Header("Minuman"))
            combined.addAll(minuman.map { MenuListItem.Menu(it) })
        }

        menuAdapter.submitList(combined)
        updateTotalPrice(menus)
    }

    private fun seedInitialData() {
        val defaultMenus = listOf(
            MenuItem(name = "Nasi Goreng", price = 15000, imageResId = R.drawable.nasi_goreng, category = "Makanan"),
            MenuItem(name = "Mie Ayam", price = 12000, imageResId = R.drawable.mie_ayam, category = "Makanan"),
            MenuItem(name = "Sate Ayam", price = 20000, imageResId = R.drawable.sate_ayam, category = "Makanan"),
            MenuItem(name = "Es Teh", price = 2000, imageResId = R.drawable.es_teh, category = "Minuman"),
            MenuItem(name = "Jus Jeruk", price = 5000, imageResId = R.drawable.es_jeruk, category = "Minuman"),
            MenuItem(name = "Es Buah", price = 12000, imageResId = R.drawable.es_buah, category = "Minuman")
        )
        defaultMenus.forEach { viewModel.insertMenu(it) }
    }

    private fun updateTotalPrice(menuList: List<MenuItem>) {
        val total = menuList.sumOf { it.price * it.quantity }
        val formatted = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(total)
        binding.textTotal.text = "Total: $formatted"
    }

    private fun updateTotalPriceFromMenus() {
        viewModel.menuList.value?.let { updateTotalPrice(it) }
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val selectedMenus = viewModel.menuList.value?.filter { it.quantity > 0 } ?: emptyList()

            if (selectedMenus.isEmpty()) {
                binding.textResult.text = "Belum ada pesanan yang dipilih."
                binding.textResult.visibility = android.view.View.VISIBLE
                return@setOnClickListener
            }

            val total = selectedMenus.sumOf { it.price * it.quantity }
            val itemsSummary = selectedMenus.joinToString("\n") {
                "- ${it.name} x ${it.quantity} = Rp ${it.price * it.quantity}"
            }

            val order = OrderHistory(
                timestamp = System.currentTimeMillis(),
                itemsSummary = itemsSummary,
                totalPrice = total
            )

            viewModel.insertOrder(order)

            selectedMenus.forEach {
                val resetItem = it.copy(quantity = 0)
                viewModel.updateMenu(resetItem)
            }

            val intent = Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
