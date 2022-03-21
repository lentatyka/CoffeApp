package com.example.coffe.model.responces

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("tokenLifeTime")
    val tokenLifeTime: Long
)