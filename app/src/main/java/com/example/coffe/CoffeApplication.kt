package com.example.coffe

import android.app.Application
import com.example.coffe.util.Constants
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoffeApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(Constants.YANDEX_API_KEY)
    }
}