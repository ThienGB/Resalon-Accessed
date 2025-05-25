package com.example.reservationdemo.data.api.model

data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String,
    val phone: String? = null,
    val address: String? = null,
    val avata: String? = null,
    val createdAt: String? = null
)

data class RegisterResponse(
    val status: Int? = null,
    val data: User? = null,
    val message: String? = null,
    val errcode: Int? = null,
)

data class LoginResponse(
    val errcode: Int? = null,
    val access_token: String? = null,
    val user: User? = null,
    val message: String? = null,
    val status: Int? = null,
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String?,
    val email: String,
    val password: String,
    val phone: String?,
    val address: String?
)