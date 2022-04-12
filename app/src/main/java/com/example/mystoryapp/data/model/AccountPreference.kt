package com.example.mystoryapp.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountPreference private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<AccountModel> {
        return dataStore.data.map { preferences ->
            AccountModel(
                preferences[USERID_KEY] ?: "",
                preferences[NAME_KEY] ?:"",
//                preferences[EMAIL_KEY] ?:"",
//                preferences[PASSWORD_KEY] ?:"",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun clearUser(){
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun saveUser(account: AccountModel) {
        dataStore.edit { preferences ->
            preferences[USERID_KEY] = account.userId
            preferences[NAME_KEY] = account.name
//            preferences[EMAIL_KEY] = account.email
//            preferences[PASSWORD_KEY] = account.password
            preferences[TOKEN_KEY] = account.token
            preferences[STATE_KEY] = account.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: AccountPreference? = null

        private val USERID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
//        private val EMAIL_KEY = stringPreferencesKey("email")
//        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): AccountPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = AccountPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}