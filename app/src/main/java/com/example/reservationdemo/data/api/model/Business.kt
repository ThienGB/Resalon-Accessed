package com.example.reservationdemo.data.api.model

import com.google.gson.annotations.SerializedName

data class Business(
    @SerializedName("_id")
    val id: String? = null,
    val name: String? = null,
    val address: String? = null,
    val description: String? = null,
    val image: String? = null,
    val ratings: List<Rating> = emptyList(),
    val services: List<ServiceInBussiness> = emptyList(),
    val averageRating: Double? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class ServiceInBussiness(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val price: Double? = null,
    val ratings: List<Rating> = emptyList(),
    val averageRating: Double? = null,
)
data class BusinessResponse(
    val data: List<Business>? = null,
    val message: String? = null,
    val status: Int? = null
)

