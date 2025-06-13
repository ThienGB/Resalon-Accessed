package com.example.reservationdemo.data.local.store

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.reservationdemo.helper.Constant.TOKEN_KEY
import com.example.reservationdemo.helper.Constant.USER_ID_KEY
import com.example.reservationdemo.helper.Constant.USER_PREF
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.userDataStore by preferencesDataStore(USER_PREF)

class UserPreferences(private val context: Context) {

    private val tokenKey = stringPreferencesKey(TOKEN_KEY)
    private val userIdKey = stringPreferencesKey(USER_ID_KEY)

    val userToken: Flow<String?> = context.userDataStore.data
        .map { preferences ->
            preferences[tokenKey]
        }

    val userId: Flow<String?> = context.userDataStore.data
        .map { preferences ->
            preferences[userIdKey]
        }
    val password: Flow<String?> = context.userDataStore.data
        .map { preferences ->
            preferences[userIdKey]
        }

    suspend fun saveUser(token: String, userId: String) {
        context.userDataStore.edit { preferences ->
            preferences[tokenKey] = token
            preferences[userIdKey] = userId
        }
    }
    suspend fun clear() {
        context.userDataStore.edit { it.clear() }
    }
}
