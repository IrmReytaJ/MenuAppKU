package com.example.menuapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.menuapp.databinding.ItemHeaderBinding
import com.example.menuapp.databinding.ItemMenuBinding
import java.text.NumberFormat
import java.util.*

sealed class MenuListItem {
    data class Header(val title: String) : MenuListItem()
    data class Menu(val item: MenuItem) : MenuListItem()
}

class MenuAdapter(
    private val onQuantityChanged: (MenuItem) -> Unit
) : ListAdapter<MenuListItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuListItem>() {
            override fun areItemsTheSame(oldItem: MenuListItem, newItem: MenuListItem): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: MenuListItem, newItem: MenuListItem): Boolean = oldItem == newItem
        }

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_MENU = 1
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is MenuListItem.Header -> VIEW_TYPE_HEADER
        is MenuListItem.Menu -> VIEW_TYPE_MENU
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(binding)
            }
            else -> {
                val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MenuViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is MenuListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is MenuListItem.Menu -> (holder as MenuViewHolder).bind(item.item)
        }
    }

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuListItem.Header) {
            binding.textHeader.text = item.title
        }
    }

    inner class MenuViewHolder(private val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem) {
            with(binding) {
                textName.text = item.name
                textPrice.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(item.price)
                imageMenu.setImageResource(item.imageResId)
                textQuantity.text = item.quantity.toString()

                buttonPlus.setOnClickListener {
                    val updated = item.copy(quantity = item.quantity + 1)
                    onQuantityChanged(updated)
                }

                buttonMinus.setOnClickListener {
                    if (item.quantity > 0) {
                        val updated = item.copy(quantity = item.quantity - 1)
                        onQuantityChanged(updated)
                    }
                }
            }
        }
    }
}