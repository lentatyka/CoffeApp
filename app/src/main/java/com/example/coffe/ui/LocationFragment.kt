package com.example.coffe.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe.CoffeViewModel
import com.example.coffe.adapters.LocationAdapter
import com.example.coffe.databinding.FragmentLocationBinding
import com.example.coffe.util.LocationService
import com.example.coffe.util.State
import com.example.coffe.util.launchWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : Fragment(), LocationService.Callback {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoffeViewModel by activityViewModels()

    @Inject
    lateinit var locationService: LocationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        locationService.startUpdate()
    }

    override fun onPause() {
        super.onPause()
//        locationService.stopUpdate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val localAdapter = LocationAdapter{
            val action = LocationFragmentDirections.actionLocationFragmentToMenuFragment(it)
            findNavController().navigate(action)
        }
        binding.apply {
            locationRecycler.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            locationRecycler.adapter = localAdapter

            btnShowMap.setOnClickListener {
                //temp
                locationService.startUpdate()
            }

            //temp
            viewModel.signIn("d", "Ds")

        }
        viewModel.state.onEach {
            if(it is State.Success){
                localAdapter.submitList(it.result)
            }
        }.launchWhenStarted(lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /*
    Callback from location service
    null -> GPS was turned off
     */
    override fun invoke(location: Location?) {
        location?.let {
            viewModel.getLocations(location)
        } ?: kotlin.run {
            Toast.makeText(requireContext(), "DISCONNECTED", Toast.LENGTH_LONG).show()
        }
    }
}