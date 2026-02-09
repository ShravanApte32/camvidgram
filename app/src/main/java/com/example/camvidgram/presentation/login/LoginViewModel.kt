package com.example.camvidgram.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camvidgram.domain.models.LoginState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> =  _loginState.asStateFlow()

    fun login(email : String, pass : String){
        if (email.isEmpty() || pass.isEmpty()) {
            _loginState.value = LoginState.Error("Email and password are required")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            delay(2000)

            if (email == "sapte32@gmail.com" && pass == "Shravan32apte"){
                _loginState.value = LoginState.Success("Login Successful!")
            } else {
                _loginState.value = LoginState.Error("Invalid Credentials!")
            }
        }
    }

    fun resetState(){
        _loginState.value = LoginState.Idle
    }
}