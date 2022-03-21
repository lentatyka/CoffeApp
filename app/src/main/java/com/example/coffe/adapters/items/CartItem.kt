package com.example.coffe.adapters.items

data class CartItem(
    override val id: Double,
    override val name: String,
    val price: Int,
    private val _amount: Int
    ) :Result {
    var amount:Int = _amount
        set(value) {
            field = if(value < 0)  0 else  value
        }
}