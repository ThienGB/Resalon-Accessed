package com.example.reservationdemo.data.local.store

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.appDataStore by preferencesDataStore(name = "app_prefs")

class AppPreferences(private val context: Context) {

    private val IS_FIRST = booleanPreferencesKey("is_first")
    private val SEARCH_HISTORY = stringPreferencesKey("search_history")

    val isFirst: Flow<Boolean?> = context.appDataStore.data
        .map { preferences ->
            preferences[IS_FIRST] ?: true
        }

    val searchHistory: Flow<List<String>> = context.appDataStore.data
        .map { preferences ->
            preferences[SEARCH_HISTORY]?.split(";") ?: emptyList()
        }
    suspend fun saveSearchHistory(newHistory: List<String>) {
        context.appDataStore.edit { preferences ->
            preferences[SEARCH_HISTORY] = newHistory.joinToString(";")
        }
    }

    suspend fun saveAppState(isFirst: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[IS_FIRST] = isFirst
        }
    }
    suspend fun clearSearchHistory() {
        context.appDataStore.edit { preferences ->
            preferences.remove(SEARCH_HISTORY)
        }
    }
    suspend fun clear() {
        context.appDataStore.edit { it.clear() }
    }
}
