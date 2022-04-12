package com.example.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.model.AccountModel
import com.example.mystoryapp.data.model.AccountPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: AccountPreference): ViewModel() {
    fun getUser(): LiveData<AccountModel> {
        return pref.getUser().asLiveData()
    }
    fun logoutUser(){
        viewModelScope.launch {
            pref.clearUser()
        }
    }
}