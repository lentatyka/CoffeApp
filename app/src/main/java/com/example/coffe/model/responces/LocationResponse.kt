package com.example.coffe.model.responces

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("id")
    val id: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("point")
    val point: Point
)
