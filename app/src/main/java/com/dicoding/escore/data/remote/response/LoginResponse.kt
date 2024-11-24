package com.dicoding.escore.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginResult: LoginResult,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class LoginResult(

    @field:SerializedName("fullName")
    val fullName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

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
