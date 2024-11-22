package com.dicoding.escore.data.remote.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
