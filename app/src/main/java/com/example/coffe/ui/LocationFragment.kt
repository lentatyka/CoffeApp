package com.example.coffe.ui

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe.R
import com.example.coffe.adapters.LocationAdapter
import com.example.coffe.databinding.FragmentLocationBinding
import com.example.coffe.util.LocationService
import com.example.coffe.util.State
import com.example.coffe.util.launchWhenStarted
import com.example.coffe.util.showToast
import com.example.coffe.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class LocationFragment : Fragment(), LocationService.Callback {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var localAdapter: LocationAdapter

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
        locationService.startUpdate()
    }

    override fun onPause() {
        super.onPause()
        locationService.stopUpdate()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setViewModel()
    }

    private fun setViewModel() {
        viewModel.setLocation()
        viewModel.state.onEach { state ->
            when (state) {
                is State.Loading -> {
                    binding.locationSwipeLayout.isRefreshing = true
                }
                is State.Error -> {
                    binding.locationSwipeLayout.isRefreshing = false
                    state.e.message!!.showToast(requireContext())
                }
                is State.Success -> {
                    binding.locationSwipeLayout.isRefreshing = false
                    localAdapter.submitList(state.result.toList())
                }
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun setAdapter() {
        localAdapter = LocationAdapter {
            val action = LocationFragmentDirections.actionLocationFragmentToMenuFragment(it)
            findNavController().navigate(action)
        }
        with(binding) {
            locationRecycler.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            locationRecycler.adapter = localAdapter
            locationSwipeLayout.setOnRefreshListener {
                locationService.startUpdate()
            }

            btnShowMap.setOnClickListener {
                val uri: Uri =
                    Uri.parse("yandexmaps://maps.yandex.ru/?ll=37.619902,55.753716&z=11&l=map")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    Callback from location service
    null -> GPS was turned off
     */
    override fun invoke(location: Location?) {
        location?.let {
            viewModel.updateLocation(location)
        } ?: getString(R.string.gps_no_connect).showToast(requireContext())
    }
}