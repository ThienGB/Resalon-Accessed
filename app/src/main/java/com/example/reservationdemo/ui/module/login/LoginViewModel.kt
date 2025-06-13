package com.example.reservationdemo.ui.module.login

import android.app.Application
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservationdemo.data.api.manager.ApiManager
import com.example.reservationdemo.data.api.manager.ApiResult
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.local.store.AppPreferences
import com.example.reservationdemo.data.local.store.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(
    application: Application,
    private val apiManager: ApiManager
) : ViewModel() {
    private val _loginResult = MutableStateFlow<ApiResult<LoginResponse>?>(null)
    val loginResult: StateFlow<ApiResult<LoginResponse>?> = _loginResult.asStateFlow()

    private val _registerResult = MutableStateFlow<ApiResult<RegisterResponse>>(ApiResult.Loading)
    val registerResult: StateFlow<ApiResult<RegisterResponse>> = _registerResult.asStateFlow()

    private val userPrefs = UserPreferences(application)
    private val appPrefs = AppPreferences(application)

    val isFirst = appPrefs.isFirst
    val userToken = userPrefs.userToken

    fun saveLoginInfo(token: String, userId: String) {
        viewModelScope.launch {
            userPrefs.saveUser(token, userId)
        }
    }
    fun isTokenExpired(token: String): Boolean {
        try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val decodedPayload = String(decodedBytes, Charsets.UTF_8)

            val json = JSONObject(decodedPayload)
            val exp = json.optLong("exp", 0L)
            if (exp == 0L) return true

            val nowSeconds = System.currentTimeMillis() / 1000
            return nowSeconds >= exp
        } catch (e: Exception) {
            e.printStackTrace()
            return true
        }
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