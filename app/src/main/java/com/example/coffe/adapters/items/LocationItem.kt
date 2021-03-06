package com.example.coffe.adapters.items

data class LocationItem(
    override val id: Double,
    override val name: String,
    val latitude: Double,
    val longitude: Double,
    val distance: Int
): Result