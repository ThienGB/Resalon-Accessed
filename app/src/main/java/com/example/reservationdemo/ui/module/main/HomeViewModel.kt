package com.example.reservationdemo.ui.module.main

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservationdemo.data.api.RetrofitClient
import com.example.reservationdemo.data.api.model.Business
import com.example.reservationdemo.data.api.model.Category
import com.example.reservationdemo.data.api.model.CityGroup
import com.example.reservationdemo.data.api.model.FavoriteRequest
import com.example.reservationdemo.data.api.model.Individual
import com.example.reservationdemo.data.api.model.LoginRequest
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.api.model.SearchResultItem
import com.example.reservationdemo.data.api.model.Service
import com.example.reservationdemo.data.api.model.User
import com.example.reservationdemo.data.local.store.AppPreferences
import com.example.reservationdemo.data.local.store.UserPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.Normalizer

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

    private val _servicesByCity = MutableStateFlow<List<CityGroup>>(emptyList())
    val servicesByCity: StateFlow<List<CityGroup>> = _servicesByCity

    private val _user = MutableStateFlow<User>(User())
    val user: StateFlow<User> = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val searchText = MutableStateFlow("Search for anything")
    var searchLocation = MutableStateFlow("")
    val location = MutableStateFlow("")

    private val userPrefs = UserPreferences(context)
    private val preferences = AppPreferences(context)

    val userToken = userPrefs.userToken

    var searchResults = MutableStateFlow<List<SearchResultItem>>(emptyList())
    val isSearchLoading = mutableStateOf(false)
    val isDropdownVisible = derivedStateOf {
        searchText.value.length >= 2 && searchResults.value.isNotEmpty()
    }
    private var searchJob: Job? = null
    fun search(query: String) {
        val raw =  removeDiacritics(searchLocation.value)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400) // debounce thủ công
            isSearchLoading.value = true
            try {
                val result = RetrofitClient.apiService.search(query, raw)
                searchResults.value = result.result ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                searchResults.value = emptyList()
            } finally {
                isSearchLoading.value = false
            }
        }
    }
    fun onSearchTextChange(newText: String) {
        searchText.value = newText

        if (newText.length >= 2) {
            search(newText)
        } else {
            searchResults.value = emptyList()
        }
    }

    val searchHistory: StateFlow<List<String>> = preferences.searchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun addSearchTerm(term: String) {
        viewModelScope.launch {
            val current = searchHistory.value.toMutableList()
            current.remove(term)
            current.add(0, term)
            if (current.size > 10) current.removeLast()
            preferences.saveSearchHistory(current)
        }
    }
    fun clearHistory(){
        viewModelScope.launch {
            preferences.clearSearchHistory()
        }
    }


    fun fetchAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val businessResponse = RetrofitClient.apiService.getAllBusiness()
                val individualsResponse = RetrofitClient.apiService.getAllIndividuals()
                val categoriesResponse = RetrofitClient.apiService.getAllCategories()
                val servicesResponse = RetrofitClient.apiService.getAllService()
                val raw =  removeDiacritics(location.value)
                val servicesByCityResponse = RetrofitClient.apiService.getServicesByCity(raw)
                val userResponse = RetrofitClient.apiService.getUser(userToken.first()!!)

                _servicesByCity.value = servicesByCityResponse.body()?.data ?: emptyList()
                _businesses.value = businessResponse.data ?: emptyList()
                _individuals.value = individualsResponse.data ?: emptyList()
                _categories.value = categoriesResponse.data ?: emptyList()
                _services.value = servicesResponse.data ?: emptyList()
                _user.value = userResponse.data ?: User()

            } catch (e: Exception) {
                _errorMessage.value = "Lỗi khi gọi API: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchServiceByCity(){
        viewModelScope.launch {
            val raw = removeDiacritics(location.value)
            val servicesByCityResponse = RetrofitClient.apiService.getServicesByCity(raw)
            _servicesByCity.value = servicesByCityResponse.body()?.data ?: emptyList()
        }
    }
    fun removeDiacritics(input: String): String {
        // Chuẩn hóa về NFD rồi loại bỏ các dấu kết hợp
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

    fun addToFavorite(type: String, favoriteId: String) {
        viewModelScope.launch {
            val request = FavoriteRequest(type, favoriteId)
            val response = RetrofitClient.apiService.addFavorite(userToken.first()!!, request)
            if (response.isSuccessful) {
                val body = response.body()
                println("Add favorite success: ${body?.message}")
            } else {
                println("Add favorite failed: ${response.errorBody()?.string()}")
            }
        }
    }

    fun removeFromFavorite(type: String, favoriteId: String) {
        viewModelScope.launch {
            val request = FavoriteRequest(type, favoriteId)
            val response = RetrofitClient.apiService.deleteFavorite(userToken.first()!!, request)
            if (response.isSuccessful) {
                val body = response.body()
                println("Delete favorite success: ${body?.message}")
            } else {
                println("Delete favorite failed: ${response.errorBody()?.string()}")
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