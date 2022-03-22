package com.example.coffe.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.coffe.model.responces.AuthBody
import com.example.coffe.repository.CoffeRepository
import com.example.coffe.util.SessionManager
import com.example.coffe.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: CoffeRepository,
    private val userManager: SessionManager,
    app: Application
) : AndroidViewModel(app) {

    fun signUp(login: String, password: String) = flow {
        emit(State.Loading)
        repository.signUp(
            AuthBody(login, password)
        ).also { result ->
            if (result.isSuccessful && result.code() == 200) {
                emit(State.Success(Boolean))
            } else {
                emit(State.Error(Exception(result.message())))
            }
        }
    }

    fun signIn(login: String, password: String) = flow {
        emit(State.Loading)
        repository.signIn(
            AuthBody(login, password)
        ).also { result ->
            if (result.isSuccessful && result.code() == 200) {
                result.body()?.let {
                    userManager.saveToken(it.token!!, it.tokenLifeTime)
                }
                emit(State.Success(Boolean))
            } else {
                emit(State.Error(Exception(result.message())))
            }
        }
    }

    fun isEmailValid(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String, c_password: String?): Boolean {
        return c_password?.let {
            password.isNotEmpty() && c_password.isNotEmpty() && password == c_password
        } ?: password.isNotEmpty()
    }
}