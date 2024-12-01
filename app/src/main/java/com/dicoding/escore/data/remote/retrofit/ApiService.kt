package com.dicoding.escore.data.remote.retrofit

import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.data.remote.response.LoginResponse
import com.dicoding.escore.data.remote.response.ModelMachineLearningResponse
import com.dicoding.escore.data.remote.response.SignUpResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignUpResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("predict")
    suspend fun predict(
        @Field("user_email") userEmail: String,
        @Field("title") title: String,
        @Field("essay") essay: String
    ): ModelMachineLearningResponse

    @GET("history/{email}")
    suspend fun history(
        @Path("email") email: String,
        @Query("createdAt") createdAt: String,
        @Query("title") title: String,
        @Query("score") score: String
    ): HistoryResponse
}