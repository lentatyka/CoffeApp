package com.example.coffe.adapters.items

data class MenuItem(
    override val id: Double,
    override val name: String,
    val imageURL: String,
    val price: Double,
): Result {
    var amount:Int = 0
    set(value) {
        field = if(value < 0)  0 else  value
    }
}
