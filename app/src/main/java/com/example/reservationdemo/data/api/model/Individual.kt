package com.example.reservationdemo.data.api.model

import com.google.gson.annotations.SerializedName


data class Individual(
    @SerializedName("_id")
    val id: String? = null,
    val name: String,
    val rating: List<Rating> = emptyList(),
    val description: String? = null,
    val image: String? = null,
    val businessId: Business? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)