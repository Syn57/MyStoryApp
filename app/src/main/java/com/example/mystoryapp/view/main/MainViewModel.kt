package com.example.mystoryapp.view.main

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.data.model.AccountModel
import com.example.mystoryapp.data.model.AccountPreference
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.view.login.LoginViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: AccountPreference): ViewModel() {

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorCode = MutableLiveData<Int>()
    val errorCode: LiveData<Int> = _errorCode

    fun getStory(token: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStories(token)
        client.enqueue(object: Callback<StoryResponse>{
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listStory.value = response.body()?.listStory
                } else {
                    _errorCode.value = response.code()
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.code()}")
                    Log.e(TAG, "onFailure: ${response.errorBody()}")
                    _errorCode.value = -1
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun getUser(): LiveData<AccountModel> {
        return pref.getUser().asLiveData()
    }
    fun logoutUser(){
        viewModelScope.launch {
            pref.clearUser()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}