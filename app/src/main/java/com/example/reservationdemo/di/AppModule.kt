package com.example.reservationdemo.di

import com.example.reservationdemo.data.api.ApiService
import com.example.reservationdemo.data.api.manager.ApiManager
import com.example.reservationdemo.data.local.store.AppPreferences
import com.example.reservationdemo.data.local.store.UserPreferences
import com.example.reservationdemo.helper.Constant.BASE_URL
import com.example.reservationdemo.ui.module.home.HomeViewModel
import com.example.reservationdemo.ui.module.auth.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val myAppModules = module {
    single<Retrofit> {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }

    single<ApiManager> {
        ApiManager(get())
    }

    single<AppPreferences> {
        AppPreferences(get())
    }

    single<UserPreferences> {
        UserPreferences(get())
    }

    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
}