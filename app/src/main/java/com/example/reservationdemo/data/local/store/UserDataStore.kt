package com.example.reservationdemo.data.local.store

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    private val TOKEN_KEY = stringPreferencesKey("user_token")
    private val USER_ID_KEY = stringPreferencesKey("user_id")

    val userToken: Flow<String?> = context.userDataStore.data
        .map { preferences ->
            preferences[TOKEN_KEY]
        }

    val userId: Flow<String?> = context.userDataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }
    val password: Flow<String?> = context.userDataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }

    suspend fun saveUser(token: String, userId: String) {
        context.userDataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[USER_ID_KEY] = userId
        }
    }
    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }
}
