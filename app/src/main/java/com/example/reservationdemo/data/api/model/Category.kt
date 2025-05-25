package com.example.reservationdemo.data.api.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("_id")
    val id: String? = null,

    val name: String? = null,
    val address: String? = null,
    val description: String? = null,
    val image: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
