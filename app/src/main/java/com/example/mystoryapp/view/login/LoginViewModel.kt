package com.example.mystoryapp.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.model.AccountModel
import com.example.mystoryapp.data.model.AccountPreference
import retrofit2.Callback
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.LoginResult
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(private val pref: AccountPreference): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //di github user pake var
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

//    var errorCode: Int = 0
    private val _errorCode = MutableLiveData<Int>()
    val errorCode: LiveData<Int> = _errorCode


    fun loginAccount(email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().loginAccount(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    _loginResult.value = response.body()?.loginResult
                } else {
                    _errorCode.value = response.code()
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.code()}")
                    Log.e(TAG, "onFailure: ${response.errorBody()}")
                    _errorCode.value = -1
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    fun saveAccount(account: AccountModel){
        viewModelScope.launch {
            pref.saveUser(account)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}