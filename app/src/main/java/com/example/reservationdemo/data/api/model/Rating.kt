package com.example.reservationdemo.data.api.model

data class Rating(
    val idUser: String,
    val rate: Int,
    val content: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)