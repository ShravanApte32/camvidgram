package com.example.camvidgram.presentation.registeruser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
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

class RegisterUserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.getInstance(application).userDao()
    private val _registerUserState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerUserState: StateFlow<RegisterState> = _registerUserState.asStateFlow()

    fun registerUser(username: String, email: String, password: String, confirmPassword: String){
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _registerUserState.value = RegisterState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            _registerUserState.value = RegisterState.Loading

            runCatching {
                delay(2000)
                userDao.insertUser(UserEntity(username = username, email = email, password = password))
            }.onSuccess {
                _registerUserState.value = RegisterState.Success("User registered successfully!")
            }.onFailure { throwable ->
                _registerUserState.value = RegisterState.Error(
                    throwable.message ?: "Error registering user"
                )
            }
        }
    }

    fun resetState(){
        _registerUserState.value = RegisterState.Idle
    }


}