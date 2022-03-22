package com.example.coffe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffe.R
import com.example.coffe.adapters.MenuAdapter
import com.example.coffe.databinding.FragmentMenuBinding
import com.example.coffe.util.State
import com.example.coffe.util.launchWhenStarted
import com.example.coffe.util.showToast
import com.example.coffe.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val navigationArgs: MenuFragmentArgs by navArgs()
    private lateinit var menuAdapter: MenuAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        (activity as MainActivity).apply {
            findViewById<TextView>(R.id.toolbar_title)?.text = getString(R.string.f_menu)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setViewModel()
    }

    private fun setViewModel() {
        viewModel.getMenu(navigationArgs.id)
        viewModel.state.onEach {
            if (it is State.Success) {
                menuAdapter.submitList(it.result)
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun setAdapter() {
        menuAdapter = MenuAdapter { id, amount ->
            viewModel.setMenuAmount(id, amount)
        }
        with(binding) {
            menuRecycler.layoutManager = GridLayoutManager(
                requireContext(),
                2
            )
            menuRecycler.adapter = menuAdapter
            btnShowCart.setOnClickListener {
                if (viewModel.cartNotEmpty())
                    MenuFragmentDirections.actionMenuFragmentToCartFragment().also {
                        findNavController().navigate(it)
                    } else
                    getString(R.string.order_failed).showToast(requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}