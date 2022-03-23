package com.example.coffe.repository

import com.example.coffe.model.CoffeService
import com.example.coffe.model.responces.AuthBody
import javax.inject.Inject

class CoffeRepository @Inject constructor(private val service: CoffeService) {
    suspend fun signUp(authBody: AuthBody) = service.signUp(authBody)
    suspend fun signIn(authBody: AuthBody) = service.signIn(authBody)
    suspend fun getLocations() = service.getLocations()
    suspend fun getMenu(id: Long) = service.getMenu(id)
}