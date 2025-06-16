package com.example.reservationdemo.ui.module.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservationdemo.data.api.manager.ApiManager
import com.example.reservationdemo.data.api.manager.ApiResult
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.local.store.AppPreferences
import com.example.reservationdemo.data.local.store.UserPreferences
import com.example.reservationdemo.helper.AuthUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPrefs: UserPreferences,
    appPrefs: AppPreferences,
    private val apiManager: ApiManager
) : ViewModel() {
    private val _loginResult = MutableStateFlow<ApiResult<LoginResponse>?>(null)
    val loginResult: StateFlow<ApiResult<LoginResponse>?> = _loginResult.asStateFlow()

    private val _registerResult = MutableStateFlow<ApiResult<RegisterResponse>>(ApiResult.Loading)
    val registerResult: StateFlow<ApiResult<RegisterResponse>> = _registerResult.asStateFlow()

    val isFirst = appPrefs.isFirst
    val userToken = userPrefs.userToken

    fun saveLoginInfo(token: String, userId: String) {
        viewModelScope.launch {
            userPrefs.saveUser(token, userId)
        }
    }
    fun isTokenExpired(token: String): Boolean {
        return AuthUtils.isJwtExpired(token)
    }
    fun clearToken() {
        viewModelScope.launch {
            userPrefs.clear()
        }
    }
    fun clearRegister() {
        viewModelScope.launch {
            _registerResult.value = ApiResult.Loading
        }
    }
    fun clearLogin() {
        viewModelScope.launch {
            _loginResult.value = null
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = ApiResult.Loading
            val result = apiManager.login(email, password)
            _loginResult.value = result
        }
    }

    fun register(name: String?, email: String, password: String, phone: String?, address: String?) {
        viewModelScope.launch {
            _registerResult.value = ApiResult.Loading
            val result = apiManager.register(RegisterRequest(name, email, password, phone, address))
            _registerResult.value = result
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}