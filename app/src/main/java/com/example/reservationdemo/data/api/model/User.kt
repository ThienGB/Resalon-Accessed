package com.example.reservationdemo.data.api.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val avatar: String? = null,
    val createdAt: String? = null,
    val favorite: UserFavorite? = null,
)

data class RegisterResponse(
    val status: Int? = null,
    val data: User? = null,
    val message: String? = null,
)
data class UserResponse(
    val status: Int? = null,
    val data: User? = null,
    val message: String? = null,
)

data class LoginResponse(
    @SerializedName("access_token")
    val token: String? = null,
    val user: User? = null,
    val message: String? = null,
    val status: Int? = null,
)

data class UserFavorite(
    val business: List<String>? = null,
    val individual: List<String>? = null,
)
data class RegisterRequest(
    val name: String?,
    val email: String,
    val password: String,
    val phone: String?,
    val address: String?
)