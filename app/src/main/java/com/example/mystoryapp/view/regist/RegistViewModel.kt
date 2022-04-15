package com.example.mystoryapp.view.regist

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.R
import retrofit2.Callback
import com.example.mystoryapp.data.model.AccountPreference
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.RegistResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.view.login.LoginViewModel
import retrofit2.Call
import retrofit2.Response

class RegistViewModel(private val pref: AccountPreference): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registResponse = MutableLiveData<RegistResponse>()
    val registResponse: LiveData<RegistResponse> = _registResponse

    private val _errorCode = MutableLiveData<Int>()
    val errorCode: LiveData<Int> = _errorCode

    fun registAccount(name: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().registAccount(name, email, password)
        client.enqueue(object: Callback<RegistResponse>{
            override fun onResponse(call: Call<RegistResponse>, response: Response<RegistResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _registResponse.value = response.body()
                } else {
                    _errorCode.value = response.code()
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.code()}")
                    Log.e(TAG, "onFailure: ${response.errorBody()}")
                    _errorCode.value = -1
                }
            }

            override fun onFailure(call: Call<RegistResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}", )
            }
        })
    }
    companion object {
        private const val TAG = "RegistViewModel"
    }
}