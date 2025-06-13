package com.example.reservationdemo.data.api.model

data class FavoriteRequest(
    val type: String,
    val favoriteId: String
)
data class GenericResponse(
    val status: Int,
    val message: String,
    val data: Any? = null
)