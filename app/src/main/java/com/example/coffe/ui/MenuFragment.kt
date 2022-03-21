package com.example.coffe.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffe.CoffeViewModel
import com.example.coffe.adapters.MenuAdapter
import com.example.coffe.databinding.FragmentMenuBinding
import com.example.coffe.util.State
import com.example.coffe.util.launchWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoffeViewModel by activityViewModels()
    private val navigationArgs: MenuFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuAdapter = MenuAdapter{id, amount ->
            viewModel.setMenuAmount(id, amount)
        }
        binding.apply {
            menuRecycler.layoutManager = GridLayoutManager(
                requireContext(),
                2
            )
            menuRecycler.adapter = menuAdapter
            btnShowCart.setOnClickListener {
                MenuFragmentDirections.actionMenuFragmentToCartFragment().also{
                    findNavController().navigate(it)
                }
            }
        }
        viewModel.getMenu(navigationArgs.id)
        viewModel.state.onEach {
            if(it is State.Success){
                menuAdapter.submitList(it.result)
            }
        }.launchWhenStarted(lifecycleScope)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}