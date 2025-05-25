package com.example.reservationdemo.data.api

import com.example.reservationdemo.data.api.model.Business
import com.example.reservationdemo.data.api.model.Category
import com.example.reservationdemo.data.api.model.Individual
import com.example.reservationdemo.data.api.model.LoginRequest
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.api.model.Service
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/schedule/mobile/{id}")
    suspend fun getScheduleById(
        @Path("id") id: String
    )
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("business")
    suspend fun getAllBusiness(): List<Business>

    @GET("Individuals")
    suspend fun getAllIndividuals(): List<Individual>

    @GET("categories")
    suspend fun getAllCategories(): List<Category>

    @GET("services")
    suspend fun getAllService(): List<Service>
}