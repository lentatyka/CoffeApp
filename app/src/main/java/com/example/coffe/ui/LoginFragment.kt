package com.example.coffe.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.coffe.R
import com.example.coffe.databinding.FragmentLoginBinding
import com.example.coffe.util.State
import com.example.coffe.util.launchWhenStarted
import com.example.coffe.util.showToast
import com.example.coffe.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        (activity as LoginActivity).apply {
            findViewById<TextView>(R.id.toolbar_title)?.text = getString(R.string.f_login)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSignIn.setOnClickListener {

                if (isEmailPasswordValid()) {
                    viewModel.signIn(
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
                                navigateToLocationScreen()
                            }
                        }
                    }.launchWhenStarted(lifecycleScope)
                }
            }

            btnRegistration.setOnClickListener {
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment().also {
                    findNavController().navigate(it)
                }
            }
        }
    }

    private fun navigateToLocationScreen() {
        Intent(context, MainActivity::class.java).also {
            it.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
            startActivity(it)
        }
    }

    private fun isEmailPasswordValid(): Boolean {
        if (!viewModel.isEmailValid(binding.etEmail.text.toString())) {
            binding.etEmail.error = getString(R.string.bad_email)
            return false
        }
        if (!viewModel.isPasswordValid(binding.etPassword.text.toString(), null)) {
            binding.etPassword.error = getString(R.string.bad_password)
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}