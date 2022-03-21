package com.example.coffe.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffe.CoffeViewModel
import com.example.coffe.adapters.CartAdapter
import com.example.coffe.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CoffeViewModel by activityViewModels()

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
        binding.apply {
            cartRecycler.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            cartRecycler.adapter = cartAdapter
        }
        cartAdapter.submitList(viewModel.getCart())
        //Мониторить события не требуется, так как
        //данные не подгружаются . Только заслать список
        //В калбеке адаптера только считаем количество и итоговую сумму

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}