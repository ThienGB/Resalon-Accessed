package com.example.reservationdemo.data.api.model

data class Business(
    val name: String? = null,
    val address: String? = null,
    val description: String? = null,
    val image: String? = null,
    val rating: List<Rating> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null
)

