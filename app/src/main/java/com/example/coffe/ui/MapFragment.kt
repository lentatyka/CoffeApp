package com.example.coffe.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.coffe.R
import com.example.coffe.databinding.FragmentMapBinding
import com.example.coffe.viewmodels.MainViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(){
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    lateinit var mapView: MapView
    lateinit var mapObjects: MapObjectCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.toolbar_title)?.text = getString(R.string.f_map)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMap()
    }

    private fun setMap() {
        mapView = binding.mapview
        mapObjects = mapView.map.mapObjects
        viewModel.getLocationCollection().forEach {locationItem ->
            mapObjects.addPlacemark(
                Point(locationItem.latitude, locationItem.longitude),
                createMarker(locationItem.name)
            )
            //Спозиционировал на первой точке, а не относительно координат пользователя.
            val temp= viewModel.getLocationCollection().first()
            mapView.map.move(
                CameraPosition(Point(temp.latitude, temp.longitude), 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0f),
                null)
            //--------------
        }
    }

    private fun createMarker(name: String): ViewProvider {
        val layout = layoutInflater.inflate(R.layout.map_marker, null) as LinearLayout
        val childAt = layout.getChildAt(1) as TextView
        childAt.text = name
        return ViewProvider(layout)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}