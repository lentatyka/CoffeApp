package com.example.coffe.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe.R
import com.example.coffe.viewmodels.MainViewModel
import com.example.coffe.adapters.CartAdapter
import com.example.coffe.databinding.FragmentCartBinding
import com.example.coffe.util.showToast

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartAdapter = CartAdapter(){id, amount ->
            viewModel.setCartAmount(id, amount)
        }

        with(binding){
            cartRecycler.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            cartRecycler.adapter = cartAdapter

            btnPayment.setOnClickListener {
                val total = viewModel.getTotalPrice()
                //Показать итог
                if(total > 0){
                    binding.txtOrder.text = getString(R.string.order_success)
                    with(btnPayment){
                        text = getString(R.string.back_to_location)
                        setOnClickListener {
                            CartFragmentDirections.actionCartFragmentToLocationFragment().also {
                                findNavController().navigate(it)
                            }
                        }
                    }
                }
                else
                    getString(R.string.order_failed).showToast(requireContext())

            }
        }
        cartAdapter.submitList(viewModel.getCart())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}