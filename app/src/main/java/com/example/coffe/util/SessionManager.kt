package com.example.coffe.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.coffe.R
import javax.inject.Inject

class SessionManager(app: Application) {
    private val preferences: SharedPreferences = app.applicationContext.getSharedPreferences(
        app.applicationContext.getString(R.string.preferences),
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TOKEN = "token"
        private const val TOKENLIFETIME = "tokenlifetime"
    }

    fun saveToken(token: String, lifeTime: Int) {
        // lifetime = 1 hour
        val expirationTime = System.currentTimeMillis() + lifeTime
        preferences.edit()
            .putString(TOKEN, token)
            .putLong(TOKENLIFETIME, expirationTime)
            .apply()
    }

    fun getToken(): User {
        return User(
            preferences.getString(TOKEN, ""),
            preferences.getLong(TOKENLIFETIME, 0)
        )
    }
}