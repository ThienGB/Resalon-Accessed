package com.example.reservationdemo.ui.module.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.reservationdemo.helper.Constant.SEARCH_DEBOUNCE_MS
import com.example.reservationdemo.helper.StringUtils.removeDiacritics
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userPrefs: UserPreferences,
    private val appPrefs: AppPreferences,
    private val apiManager: ApiManager
) : ViewModel() {

    val userToken = userPrefs.userToken

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent

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
    val searchResults: StateFlow<ApiResult<SearchResponse>> = _searchResults.asStateFlow()

    private val _user = MutableStateFlow<ApiResult<UserResponse>>(ApiResult.Loading)
    val user: StateFlow<ApiResult<UserResponse>> = _user.asStateFlow()

    val searchText = MutableStateFlow("Search for anything")
    var searchLocation = MutableStateFlow("")
    val location = MutableStateFlow("")

    private var searchJob: Job? = null

    val searchHistory: StateFlow<List<String>> = appPrefs.searchHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun fetchAllData() {
        val rawLocation = removeDiacritics(location.value)

        viewModelScope.launch {
            _isLoading.value = true
            coroutineScope {
                val businessDeferred = async { apiManager.getAllBusiness() }
                val individualDeferred = async { apiManager.getAllIndividuals() }
                val categoryDeferred = async { apiManager.getAllCategories() }
                val serviceDeferred = async { apiManager.getAllServices() }
                val servicesByCityDeferred = async { apiManager.getServicesByCity(rawLocation) }

                val userTokenValue = userToken.firstOrNull()
                val userDeferred = userTokenValue?.let { token ->
                    async { apiManager.getUser(token) }
                }

                _businesses.value = businessDeferred.await()
                _individuals.value = individualDeferred.await()
                _categories.value = categoryDeferred.await()
                _services.value = serviceDeferred.await()
                _servicesByCity.value = servicesByCityDeferred.await()
                if (userDeferred != null) {
                    _user.value = userDeferred.await()
                }
            }
            _isLoading.value = false
        }
    }

    fun fetchServiceByCity() {
        val raw = removeDiacritics(location.value)
        viewModelScope.launch {
            _servicesByCity.value = ApiResult.Loading
            _servicesByCity.value = apiManager.getServicesByCity(raw)
        }
    }

    fun search(query: String) {
        val rawLocation = removeDiacritics(searchLocation.value)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS)
            _searchResults.value = ApiResult.Loading
            _searchResults.value = apiManager.search(query, rawLocation)
        }
    }

    fun onSearchTextChange(newText: String) {
        searchText.value = newText
        if (newText.length >= 2) {
            search(newText)
        } else {
            _searchResults.value = ApiResult.Loading
        }
    }

    fun addSearchTerm(term: String) {
        viewModelScope.launch {
            val current = searchHistory.value.toMutableList().apply {
                remove(term)
                add(0, term)
                if (size > 10) removeAt(lastIndex)
            }
            appPrefs.saveSearchHistory(current)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            appPrefs.clearSearchHistory()
        }
    }

    fun addToFavorite(type: String, favoriteId: String) {
        viewModelScope.launch {
            val token = userToken.firstOrNull()
            if (token.isNullOrBlank()) return@launch

            val result = apiManager.addFavorite(token, FavoriteRequest(type, favoriteId))
            if (result is ApiResult.Error) {
                _errorEvent.emit("Add favorite failed")
            }
        }
    }

    fun removeFromFavorite(type: String, favoriteId: String) {
        viewModelScope.launch {
            val token = userToken.firstOrNull()
            if (token.isNullOrBlank()) return@launch

            val result = apiManager.deleteFavorite(token, FavoriteRequest(type, favoriteId))
            if (result is ApiResult.Error) {
                _errorEvent.emit("Remove favorite failed")
            }
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            userPrefs.clear()
        }
    }
}