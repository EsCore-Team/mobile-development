package com.dicoding.escore.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("token")
    val token: String
)

//    @field:SerializedName("loginResult")
//    val loginResult: LoginResult,
//
//    @field:SerializedName("error")
//    val error: Boolean,
//
//    @field:SerializedName("message")
//    val message: String


//data class LoginResult(
//
//    @field:SerializedName("name")
//    val name: String,
//
//    @field:SerializedName("userId")
//    val userId: String,
//
//    @field:SerializedName("token")
//    val token: String
//)
