package com.example.reservationdemo.data.api.model

import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("_id")
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class ServiceResponse(
    val data: List<Service>? = null,
    val message: String? = null,
    val status: Int? = null
)

data class ServicesByCityResponse(
    val status: Int,
    val message: String,
    val data: List<CityGroup>?,
    val total: Int?,
    val totalServices: Int?
)

data class CityGroup(
    val city: String = "",
    val totalServices: Int? = null,
    val cityAverageRating: Double? = null,
    val services: List<ServiceItem>? = null
)

data class ServiceItem(
    val serviceId: String,
    val businessId: String,
    val businessName: String,
    val businessAddress: String,
    val serviceTitle: String,
    val serviceDescription: String,
    val serviceImage: String,
    val price: Double,
    val averageRating: Double,
    val ratings: List<Rating>?,
    val createdAt: String
)

