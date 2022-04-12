package com.example.mystoryapp.data.remote.retrofit

import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.RegistResponse
import retrofit2.Call
import retrofit2.http.*

//interface ApiService {
//    @POST("/login")
//    fun getLogin(
//        @Body("email") email: String,
//        @Body("password") password: String
//    ): Call<LoginResponse>
//}
interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun loginAccount(
        @Field ("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun registAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegistResponse>
}