package com.example.coffe.adapters.items

data class LocationItem(
    override val id: Double,
    override val name: String,
    val distance: Int
): Result