package com.example.coffe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe.BR
import com.example.coffe.R
import com.example.coffe.adapters.LocationAdapter.Companion.DiffCallback
import com.example.coffe.adapters.items.CartItem
import com.example.coffe.adapters.items.Result
import com.example.coffe.databinding.ItemCartBinding

class CartAdapter(
    private val callback: (Double, Int) -> Unit
) : ListAdapter<Result, CartAdapter.MenuViewHolder>(DiffCallback) {

    class MenuViewHolder(
        private val binding: ViewDataBinding,
        private val callback: (Double, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding as ItemCartBinding
            binding.setVariable(BR.cart, item)
            binding.executePendingBindings()

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
                    if (--amount >= 0) {
                        it.text = amount.toString()
                        callback(item.id, amount)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = DataBindingUtil.inflate<ItemCartBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cart,
            parent,
            false
        )
        return MenuViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position) as CartItem)
    }
}