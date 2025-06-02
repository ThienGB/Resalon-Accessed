package com.example.reservationdemo.data.api.model

data class FavoriteRequest(
    val type: String,       // "business" hoặc "individual"
    val favoriteId: String  // ObjectId MongoDB
)
data class GenericResponse(
    val status: Int,
    val message: String,
    val data: Any? = null
)