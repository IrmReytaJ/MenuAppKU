package com.example.menuapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.databinding.ItemOrderHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryAdapter(
    private val historyList: List<OrderHistory>,
    private val onDeleteClick: (OrderHistory) -> Unit
) : RecyclerView.Adapter<OrderHistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("in", "ID"))

        with(holder.binding) {
            textTime.text = "Waktu: ${formatter.format(Date(item.timestamp))}"
            textItems.text = item.itemsSummary.ifBlank { "Detail pesanan tidak tersedia." }

            buttonDelete.setOnClickListener {
                val currentPosition = holder.bindingAdapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    onDeleteClick(historyList[currentPosition])
                }
            }
        }
    }

    override fun getItemCount(): Int = historyList.size
}
