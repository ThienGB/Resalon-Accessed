package com.example.reservationdemo.ui.module.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservationdemo.data.api.RetrofitClient
import com.example.reservationdemo.data.api.model.Business
import com.example.reservationdemo.data.api.model.Category
import com.example.reservationdemo.data.api.model.Individual
import com.example.reservationdemo.data.api.model.LoginRequest
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.api.model.Service
import com.example.reservationdemo.data.local.store.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    context: Context
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _businesses = MutableStateFlow<List<Business>>(emptyList())
    val businesses: StateFlow<List<Business>> = _businesses

    private val _individuals = MutableStateFlow<List<Individual>>(emptyList())
    val individuals: StateFlow<List<Individual>> = _individuals

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val searchText = MutableStateFlow("Search for anything")
    var searchLocation = MutableStateFlow("")
    val location = MutableStateFlow("")

    private val userPrefs = UserPreferences(context)

    fun fetchAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val businessResponse = RetrofitClient.apiService.getAllBusiness()
                val individualsResponse = RetrofitClient.apiService.getAllIndividuals()
                val categoriesResponse = RetrofitClient.apiService.getAllCategories()
                val servicesResponse = RetrofitClient.apiService.getAllService()

                _businesses.value = businessResponse
                _individuals.value = individualsResponse
                _categories.value = categoriesResponse
                _services.value = servicesResponse

            } catch (e: Exception) {
                _errorMessage.value = "Lỗi khi gọi API: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            userPrefs.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}