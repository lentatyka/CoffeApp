package com.example.coffe.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coffe.R
import com.example.coffe.databinding.FragmentRegistrationBinding
import com.example.coffe.util.State
import com.example.coffe.util.launchWhenStarted
import com.example.coffe.util.showToast
import com.example.coffe.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSignUp.setOnClickListener {
                if (isEmailPasswordValid()) {
                    viewModel.signUp(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    ).onEach { state ->
                        when (state) {
                            is State.Loading -> {
                                //showLoad
                            }
                            is State.Error ->
                                state.e.message?.showToast(requireContext())
                            is State.Success -> {
                                showSnackBar()
                            }
                        }
                    }.launchWhenStarted(lifecycleScope)
                }
            }
        }
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, getString(R.string.reg_success), Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.ok_btn) {
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment().also {
                    findNavController().navigate(it)
                }
            }.show()
    }

    private fun isEmailPasswordValid(): Boolean {
        if (!viewModel.isEmailValid(binding.etEmail.text.toString())) {
            binding.etEmail.error = getString(R.string.bad_email)
            return false
        }
        if (!viewModel.isPasswordValid(
                binding.etPassword.text.toString(),
                binding.etPasswordConfirm.text.toString()
            )
        ) {
            binding.etPasswordConfirm.error = getString(R.string.bad_password_confirm)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}