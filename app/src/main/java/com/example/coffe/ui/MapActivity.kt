package com.example.coffe.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.coffe.R
import com.example.coffe.util.Constants.YANDEX_API_KEY
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class MapActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(YANDEX_API_KEY)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_map)

        mapView = findViewById<MapView>(R.id.mapview)
        mapView.map.move(
            CameraPosition(Point(55.751574, 37.573856), 15.0f, 0.0f, 0.0f),
        Animation(Animation.Type.SMOOTH, 0f),
        null)
        //Не разобрался пока с метками
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_map_marker, theme)!!.toBitmap()
        mapView.map.mapObjects.addPlacemark(
            Point(55.751574, 37.573856),
            ImageProvider.fromBitmap(icon)
        )

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

}