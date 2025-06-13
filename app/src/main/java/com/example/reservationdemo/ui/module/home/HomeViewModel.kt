package com.example.reservationdemo.ui.module.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reservationdemo.data.api.manager.ApiError
import com.example.reservationdemo.data.api.manager.ApiManager
import com.example.reservationdemo.data.api.manager.ApiResult
import com.example.reservationdemo.data.api.model.BusinessResponse
import com.example.reservationdemo.data.api.model.CategoryResponse
import com.example.reservationdemo.data.api.model.FavoriteRequest
import com.example.reservationdemo.data.api.model.IndividualResponse
import com.example.reservationdemo.data.api.model.SearchResponse
import com.example.reservationdemo.data.api.model.ServiceResponse
import com.example.reservationdemo.data.api.model.ServicesByCityResponse
import com.example.reservationdemo.data.api.model.UserResponse
import com.example.reservationdemo.data.local.store.AppPreferences
import com.example.reservationdemo.data.local.store.UserPreferences
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.Normalizer

class HomeViewModel(
    context: Context,
    private val apiManager: ApiManager
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)

    private val _businesses = MutableStateFlow<ApiResult<BusinessResponse>>(ApiResult.Loading)
    val businesses: StateFlow<ApiResult<BusinessResponse>> = _businesses.asStateFlow()

    private val _individuals = MutableStateFlow<ApiResult<IndividualResponse>>(ApiResult.Loading)
    val individuals: StateFlow<ApiResult<IndividualResponse>> = _individuals.asStateFlow()

    private val _categories = MutableStateFlow<ApiResult<CategoryResponse>>(ApiResult.Loading)
    val categories: StateFlow<ApiResult<CategoryResponse>> = _categories.asStateFlow()

    private val _services = MutableStateFlow<ApiResult<ServiceResponse>>(ApiResult.Loading)
    val services: StateFlow<ApiResult<ServiceResponse>> = _services.asStateFlow()

    private val _servicesByCity = MutableStateFlow<ApiResult<ServicesByCityResponse>>(ApiResult.Loading)
    val servicesByCity: StateFlow<ApiResult<ServicesByCityResponse>> = _servicesByCity.asStateFlow()

    private val _searchResults = MutableStateFlow<ApiResult<SearchResponse>>(ApiResult.Loading)
    val searchResults : StateFlow<ApiResult<SearchResponse>> = _searchResults.asStateFlow()

    private val _user = MutableStateFlow<ApiResult<UserResponse>>(ApiResult.Loading)
    val user: StateFlow<ApiResult<UserResponse>> = _user.asStateFlow()

    val searchText = MutableStateFlow("Search for anything")
    var searchLocation = MutableStateFlow("")
    val location = MutableStateFlow("")

    private val userPrefs = UserPreferences(context)
    private val preferences = AppPreferences(context)

    val userToken = userPrefs.userToken

    private var searchJob: Job? = null
    fun search(query: String) {
        val raw =  removeDiacritics(searchLocation.value)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            _searchResults.value = ApiResult.Loading
            val result = apiManager.search(query, raw)
            _searchResults.value = result
        }
    }
    fun onSearchTextChange(newText: String) {
        searchText.value = newText

        if (newText.length >= 2) {
            search(newText)
        } else {
            _searchResults.value = ApiResult.Loading ///////////////// Need test //////////////
        }
    }

    val searchHistory: StateFlow<List<String>> = preferences.searchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addSearchTerm(term: String) {
        viewModelScope.launch {
            val current = searchHistory.value.toMutableList()
            current.remove(term)
            current.add(0, term)
            if (current.size > 10) current.removeAt(current.lastIndex)
            preferences.saveSearchHistory(current)
        }
    }
    fun clearHistory(){
        viewModelScope.launch {
            preferences.clearSearchHistory()
        }
    }

    fun fetchAllData() {
        val raw =  removeDiacritics(location.value)
        viewModelScope.launch {
            _isLoading.value = true
            try {
                coroutineScope {
                    val businessDeferred = async { apiManager.getAllBusiness() }
                    val individualDeferred = async { apiManager.getAllIndividuals() }
                    val categoryDeferred = async { apiManager.getAllCategories() }
                    val serviceDeferred = async { apiManager.getAllServices() }
                    val servicesByCityDeferred = async { apiManager.getServicesByCity(raw) }
                    if (userToken.first() != null) {
                        val userDeferred = async { apiManager.getUser(userToken.first()!!) }
                        _user.value = userDeferred.await()
                    }
                    _businesses.value = businessDeferred.await()
                    _individuals.value = individualDeferred.await()
                    _categories.value = categoryDeferred.await()
                    _services.value = serviceDeferred.await()
                    _servicesByCity.value = servicesByCityDeferred.await()
                }
            } catch (e: Exception) {
                val error = ApiResult.Error(ApiError(message = "Lỗi khi tải dữ liệu: ${e.localizedMessage}"))
                _businesses.value = error
                _individuals.value = error
                _categories.value = error
                _services.value = error
                _servicesByCity.value = error
                _user.value = error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchServiceByCity(){
        viewModelScope.launch {
            val raw = removeDiacritics(location.value)
            _servicesByCity.value = ApiResult.Loading
            val result = apiManager.getServicesByCity(raw)
            _servicesByCity.value = result
        }
    }

    fun removeDiacritics(input: String): String {
        // Xóa dấu
        val normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
        return normalized.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

    fun addToFavorite(type: String, favoriteId: String) {
        viewModelScope.launch {
            val token = userToken.firstOrNull()
            if (token.isNullOrBlank()) {
                println("Token không hợp lệ")
                return@launch
            }
            val request = FavoriteRequest(type = type, favoriteId = favoriteId)
            when (val result = apiManager.addFavorite(token, request)) {
                is ApiResult.Success -> {
                    println("Add favorite success: ${result.data.message}")
                }
                is ApiResult.Error -> {
                    println("Add favorite failed: ${result.error.message}")
                }
                ApiResult.Loading -> {
                }
            }
        }
    }

    fun removeFromFavorite(type: String, favoriteId: String) {
        viewModelScope.launch {
            val token = userToken.firstOrNull()
            if (token.isNullOrBlank()) {
                println("Token không hợp lệ")
                return@launch
            }
            val request = FavoriteRequest(type = type, favoriteId = favoriteId)
            when (val result = apiManager.deleteFavorite(token, request)) {
                is ApiResult.Success -> {
                    println("Delete favorite success: ${result.data.message}")
                }
                is ApiResult.Error -> {
                    println("Delete favorite failed: ${result.error.message}")
                }
                ApiResult.Loading -> {
                }
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