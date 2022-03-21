package com.example.coffe.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe.BR
import com.example.coffe.R
import com.example.coffe.adapters.items.LocationItem
import com.example.coffe.adapters.items.Result
import com.example.coffe.databinding.ItemLocationBinding

class LocationAdapter(
    private val callback: (Long)->Unit
):ListAdapter<Result, LocationAdapter.LocationViewHolder>(DiffCallback) {

    class LocationViewHolder(
        private val  binding: ViewDataBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Result){
            binding.setVariable(BR.location, item)
            binding.executePendingBindings()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = DataBindingUtil.inflate<ItemLocationBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_location,
            parent,
            false
        )
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = getItem(position) as LocationItem
        holder.bind(item)
        holder.itemView.setOnClickListener {
            callback(item.id.toLong())
        }
    }
    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Result>(){
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }

        }
    }
}