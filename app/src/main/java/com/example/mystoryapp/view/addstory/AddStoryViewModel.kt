package com.example.mystoryapp.view.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.remote.response.UploadResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {

    private val _responseMsg = MutableLiveData<String>()
    val responseMsg: LiveData<String> = _responseMsg

    private val _responseCode = MutableLiveData<Int>()
    val responseCode: LiveData<Int> = _responseCode

    private val _responseUp = MutableLiveData<UploadResponse>()
    val responseUp: LiveData<UploadResponse> = _responseUp

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun upStory(file: MultipartBody.Part, desc: RequestBody, token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().uploadImage(file, desc, token)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _responseUp.value = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _responseMsg.value = response.message()
                    }
                } else {
                    _responseMsg.value = response.message()
                    _responseCode.value = response.code()
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.code()}")
                    Log.e(TAG, "onFailure: ${response.errorBody()}")
                    _responseCode.value = -1
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _isLoading.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    companion object {
        private const val TAG = "AddStoryViewModel"
    }
}