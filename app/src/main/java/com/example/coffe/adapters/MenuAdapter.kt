package com.example.coffe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffe.BR
import com.example.coffe.R
import com.example.coffe.adapters.LocationAdapter.Companion.DiffCallback
import com.example.coffe.adapters.items.MenuItem
import com.example.coffe.adapters.items.Result
import com.example.coffe.databinding.ItemMenuBinding

class MenuAdapter(
    private val callback: (Double, Int)->Unit
):ListAdapter<Result, MenuAdapter.MenuViewHolder>(DiffCallback) {

    class MenuViewHolder(
        private val  binding: ViewDataBinding,
        private val callback: (Double, Int)->Unit
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(index: Int, item: MenuItem){
            binding as ItemMenuBinding
            binding.setVariable(BR.menu, item)
            binding.executePendingBindings()
            Glide.with(itemView)
                .load(item.imageURL)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(binding.imgProduct)
            binding.btnAdd.setOnClickListener {
                binding.txtAmount.also {
                    var amount = Integer.valueOf(it.text.toString())    //NumberFormatExc?
                    it.text = (++amount).toString()
                    callback(item.id, amount)
                }
            }
            binding.btnRemove.setOnClickListener {
                binding.txtAmount.also {
                    var amount = Integer.valueOf(it.text.toString())    //NumberFormatExc?
                    if(--amount >= 0){
                        it.text = amount.toString()
                        callback(item.id, amount)
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = DataBindingUtil.inflate<ItemMenuBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_menu,
            parent,
            false
        )
        return MenuViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position, getItem(position) as MenuItem)
    }
}