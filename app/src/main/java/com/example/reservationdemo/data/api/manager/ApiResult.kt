package com.example.reservationdemo.data.api.manager

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

data class ApiError(
    val code: Int? = null,
    val message: String,
    val throwable: Throwable? = null
)