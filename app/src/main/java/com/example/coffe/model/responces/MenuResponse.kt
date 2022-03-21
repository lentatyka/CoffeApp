package com.example.coffe.model.responces

import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("id")
    val id: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageURL")
    val imageURL: String,
    @SerializedName("price")
    val price: Double
)
