package com.example.reservationdemo.data.api

import com.example.reservationdemo.data.api.model.Business
import com.example.reservationdemo.data.api.model.BusinessResponse
import com.example.reservationdemo.data.api.model.Category
import com.example.reservationdemo.data.api.model.CategoryResponse
import com.example.reservationdemo.data.api.model.FavoriteRequest
import com.example.reservationdemo.data.api.model.GenericResponse
import com.example.reservationdemo.data.api.model.Individual
import com.example.reservationdemo.data.api.model.IndividualResponse
import com.example.reservationdemo.data.api.model.LoginRequest
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.api.model.SearchResponse
import com.example.reservationdemo.data.api.model.SearchResultItem
import com.example.reservationdemo.data.api.model.Service
import com.example.reservationdemo.data.api.model.ServiceResponse
import com.example.reservationdemo.data.api.model.ServicesByCityResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getAllBusiness(): BusinessResponse

    @GET("Individuals")
    suspend fun getAllIndividuals(): IndividualResponse

    @GET("categories")
    suspend fun getAllCategories(): CategoryResponse

    @GET("services")
    suspend fun getAllService(): ServiceResponse

    @GET("services/by-city")
    suspend fun getServicesByCity(
        @Query("city") city: String,
    ): Response<ServicesByCityResponse>

    @GET("user")
    suspend fun getUser(
        @Header("token") token: String
    ): RegisterResponse

    @POST("add-favorite")
    suspend fun addFavorite(
        @Header("token") token: String,
        @Body favoriteRequest: FavoriteRequest
    ): Response<GenericResponse>

    @POST("delete-favorite")
    suspend fun deleteFavorite(
        @Header("token") token: String,
        @Body favoriteRequest: FavoriteRequest
    ): Response<GenericResponse>

    @GET("search")
    suspend fun search(
        @Query("q") query: String,
        @Query("location") location: String
    ): SearchResponse

}