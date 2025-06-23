package com.example.menuapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.menuapp.databinding.ActivityOrderHistoryBinding

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryBinding
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)

        viewModel.orderHistoryList.observe(this) { list ->
            val adapter = OrderHistoryAdapter(list){ item ->
                viewModel.deleteOrder(item)
            }
            binding.recyclerHistory.adapter = adapter
        }
    }
}
