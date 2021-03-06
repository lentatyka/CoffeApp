package com.example.coffe.model

import com.example.coffe.model.responces.AuthBody
import com.example.coffe.model.responces.AuthResponse
import com.example.coffe.model.responces.LocationResponse
import com.example.coffe.model.responces.MenuResponse
import retrofit2.Response
import retrofit2.http.*

interface CoffeService {

    @POST("/auth/register")
    suspend fun signUp(@Body auth: AuthBody):Response<AuthResponse>

    @POST("/auth/login")
    suspend fun signIn(@Body auth: AuthBody):Response<AuthResponse>

    @GET("/locations")
    suspend fun getLocations(): Response<ArrayList<LocationResponse>>

    @GET("/location/{id}/menu")
    suspend fun getMenu(@Path("id") id: Long): Response<ArrayList<MenuResponse>>


}