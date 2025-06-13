package com.example.reservationdemo.data.api.model

import com.google.gson.annotations.SerializedName


data class Individual(
    @SerializedName("_id")
    val id: String? = null,
    val name: String,
    val ratings: List<Rating> = emptyList(),
    val description: String? = null,
    val image: String? = null,
    val businessId: Business? = null,
    val averageRating: Double? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
data class IndividualResponse(
    val data: List<Individual>? = null,
    val message: String? = null,
    val status: Int? = null
)