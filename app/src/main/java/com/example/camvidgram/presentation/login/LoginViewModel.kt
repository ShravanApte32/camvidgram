package com.example.camvidgram.presentation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.camvidgram.data.local.db.AppDatabase
import com.example.camvidgram.data.local.entities.UserEntity
import com.example.camvidgram.domain.models.LoginState
import com.example.camvidgram.domain.models.RegisterState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> =  _loginState.asStateFlow()

    fun login(email : String, pass : String){
        if (email.isEmpty() || pass.isEmpty()) {
            _loginState.value = LoginState.Error("Email and password are required")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            runCatching {
                delay(2000)
                userDao.loginUser(email,pass)
            }.onSuccess { user ->
                if (user != null) {
                    _loginState.value = LoginState.Success("Login successful!")
                } else {
                    _loginState.value = LoginState.Error("Invalid email or password")
                }

            }.onFailure { throwable ->
                _loginState.value = LoginState.Error(
                    throwable.message ?: "Error logging in"
                )
            }
        }
    }

    fun resetState(){
        _loginState.value = LoginState.Idle
    }
}