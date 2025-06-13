package com.example.reservationdemo.data.api.manager

import com.example.reservationdemo.data.api.ApiService
import com.example.reservationdemo.data.api.model.BusinessResponse
import com.example.reservationdemo.data.api.model.CategoryResponse
import com.example.reservationdemo.data.api.model.FavoriteRequest
import com.example.reservationdemo.data.api.model.GenericResponse
import com.example.reservationdemo.data.api.model.IndividualResponse
import com.example.reservationdemo.data.api.model.LoginResponse
import com.example.reservationdemo.data.api.model.RegisterRequest
import com.example.reservationdemo.data.api.model.RegisterResponse
import com.example.reservationdemo.data.api.model.SearchResponse
import com.example.reservationdemo.data.api.model.ServiceResponse
import com.example.reservationdemo.data.api.model.ServicesByCityResponse
import com.example.reservationdemo.data.api.model.UserResponse
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ApiManager(private val apiService: ApiService) {

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiResult.Success(body)
                } else {
                    ApiResult.Error(ApiError(message = "Dữ liệu trả về rỗng"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val messageFromServer = try {
                    val json = JSONObject(errorBody ?: "")
                    json.optString("message", "Lỗi không xác định từ server")
                } catch (e: Exception) {
                    println("Error parsing JSON: ${e.message}")
                    "Lỗi không xác định từ server"
                }
                ApiResult.Error(
                    ApiError(
                        code = response.code(),
                        message = messageFromServer
                    )
                )
            }
        } catch (e: IOException) {
            ApiResult.Error(ApiError(message = "Lỗi mạng, vui lòng kiểm tra kết nối", throwable = e))
        } catch (e: HttpException) {
            ApiResult.Error(
                ApiError(
                    code = e.code(),
                    message = e.message() ?: "Lỗi HTTP không xác định",
                    throwable = e
                )
            )
        } catch (e: Exception) {
            ApiResult.Error(
                ApiError(message = "Lỗi không xác định: ${e.message}", throwable = e)
            )
        }
    }

    suspend fun login(email: String, password: String): ApiResult<LoginResponse> {
        return safeApiCall { apiService.login(email, password) }
    }

    suspend fun register(request: RegisterRequest): ApiResult<RegisterResponse> {
        return safeApiCall { apiService.register(request) }
    }

    suspend fun getAllBusiness(): ApiResult<BusinessResponse> {
        return safeApiCall { apiService.getAllBusiness() }
    }

    suspend fun getAllIndividuals(): ApiResult<IndividualResponse> {
        return safeApiCall { apiService.getAllIndividuals() }
    }

    suspend fun getAllCategories(): ApiResult<CategoryResponse> {
        return safeApiCall { apiService.getAllCategories() }
    }

    suspend fun getAllServices(): ApiResult<ServiceResponse> {
        return safeApiCall { apiService.getAllService() }
    }

    suspend fun getServicesByCity(city: String): ApiResult<ServicesByCityResponse> {
        return safeApiCall { apiService.getServicesByCity(city) }
    }

    suspend fun getUser(token: String): ApiResult<UserResponse> {
        return safeApiCall { apiService.getUser(token) }
    }

    suspend fun addFavorite(token: String, favoriteRequest: FavoriteRequest): ApiResult<GenericResponse> {
        return safeApiCall { apiService.addFavorite(token, favoriteRequest) }
    }

    suspend fun deleteFavorite(token: String, favoriteRequest: FavoriteRequest): ApiResult<GenericResponse> {
        return safeApiCall { apiService.deleteFavorite(token, favoriteRequest) }
    }

    suspend fun search(query: String, location: String): ApiResult<SearchResponse> {
        return safeApiCall { apiService.search(query, location) }
    }
}