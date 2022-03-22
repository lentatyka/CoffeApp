package com.example.coffe.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.coffe.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {
    @Inject
    lateinit var userManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = userManager.getToken()
        val currentTime = System.currentTimeMillis()

        if (user.token != null && user.tokenLifeTime > currentTime)
            launchActivity(MainActivity::class.java)
        else
            launchActivity(LoginActivity::class.java)
    }

    private fun <T : Activity> launchActivity(activity: Class<T>) {
        Intent(this, activity).also { intent ->
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
            startActivity(intent)
            finish()
        }
    }
}