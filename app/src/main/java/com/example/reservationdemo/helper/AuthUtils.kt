package com.example.reservationdemo.helper

import android.util.Base64
import org.json.JSONObject

object  AuthUtils {
    fun isJwtExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val decodedPayload = String(decodedBytes, Charsets.UTF_8)

            val json = JSONObject(decodedPayload)
            val exp = json.optLong("exp", 0L)
            if (exp == 0L) return true

            val nowSeconds = System.currentTimeMillis() / 1000
            nowSeconds >= exp
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }
}