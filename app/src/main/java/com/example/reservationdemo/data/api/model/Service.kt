package com.example.reservationdemo.data.api.model

import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("_id")
    val id: String? = null,
    val title: String? = null,
    val price: Double? = null,
    val description: String? = null,
    val rating: List<Rating> = emptyList(),
    val image: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
