package com.example.reservationdemo.data.local.store

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.reservationdemo.helper.Constant.APP_PREF
import com.example.reservationdemo.helper.Constant.IS_FIRST
import com.example.reservationdemo.helper.Constant.SEARCH_HISTORY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.appDataStore by preferencesDataStore(APP_PREF)

class AppPreferences(private val context: Context) {

    private val isFirstKey = booleanPreferencesKey(IS_FIRST)
    private val searchHistoryKey = stringPreferencesKey(SEARCH_HISTORY)
    val isFirst: Flow<Boolean?> = context.appDataStore.data
        .map { preferences ->
            preferences[isFirstKey] ?: true
        }
    val searchHistory: Flow<List<String>> = context.appDataStore.data
        .map { preferences ->
            preferences[searchHistoryKey]?.split(";") ?: emptyList()
        }
    suspend fun saveSearchHistory(newHistory: List<String>) {
        context.appDataStore.edit { preferences ->
            preferences[searchHistoryKey] = newHistory.joinToString(";")
        }
    }

    suspend fun saveAppState(isFirst: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[isFirstKey] = isFirst
        }
    }
    suspend fun clearSearchHistory() {
        context.appDataStore.edit { preferences ->
            preferences.remove(searchHistoryKey)
        }
    }
    suspend fun clear() {
        context.appDataStore.edit { it.clear() }
    }
}
