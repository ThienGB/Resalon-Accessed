package com.example.reservationdemo.ui.module.login

import android.content.Context
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservationdemo.data.api.RetrofitClient
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.local.store.AppPreferences
import com.example.reservationdemo.data.local.store.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(
    private val context: Context
) : ViewModel() {
    private val _loginResult = MutableStateFlow(LoginResponse())
    val loginResult: StateFlow<LoginResponse> = _loginResult

    private val _registerResult = MutableStateFlow(RegisterResponse())
    val registerResult: StateFlow<RegisterResponse> = _registerResult

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val userPrefs = UserPreferences(context)
    private val appPrefs = AppPreferences(context)

    val isFirst = appPrefs.isFirst
    val userToken = userPrefs.userToken
    val userId = userPrefs.userId

    fun saveLoginInfo(token: String, userId: String) {
        viewModelScope.launch {
            userPrefs.saveUser(token, userId)
        }
    }
    fun isTokenExpired(token: String): Boolean {
        try {
            val parts = token.split(".")
            if (parts.size != 3) return true // không phải JWT

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
            _registerResult.value = RegisterResponse()
        }
    }
    fun clearLogin() {
        viewModelScope.launch {
            _loginResult.value = LoginResponse()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.apiService.login(email, password)
                _loginResult.value = response
                appPrefs.saveAppState(false)
            } catch (e: Exception) {
                e.printStackTrace()
                _loginResult.value = LoginResponse(
                    status = 400,
                    message = "Wrong email or password"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String?, email: String, password: String, phone: String?, address: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = RegisterRequest(name, email, password, phone, address)
                val response = RetrofitClient.apiService.register(request)
                _registerResult.value = response

            } catch (e: Exception) {
                e.printStackTrace()
                _registerResult.value = RegisterResponse()
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}