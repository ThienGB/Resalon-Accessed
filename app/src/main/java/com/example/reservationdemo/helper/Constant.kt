package com.example.reservationdemo.helper

object Constant {
    const val BASE_URL = "https://resalon.onrender.com/v1/booking/"

    // SharedPreferences
    const val IS_FIRST = "is_first"
    const val APP_PREF = "app_prefs"
    const val SEARCH_HISTORY = "search_history"
    const val TOKEN_KEY = "user_token"
    const val USER_PREF = "user_prefs"
    const val USER_ID_KEY = "user_id"


    // Status Codes
    const val STATUS_PENDING = "PENDING"
    const val STATUS_CONFIRMED = "CONFIRMED"
    const val STATUS_CANCELLED = "CANCELLED"


    // Variable
    const val SEARCH_DEBOUNCE_MS = 400L
}