package com.example.duos.data.remote.auth

import com.example.duos.data.entities.LargeLocal
import com.google.gson.annotations.SerializedName

data class Auth(@SerializedName("jwt") val jwt: String)

data class AuthResponse(
    @SerializedName("regcodes") val reg : List<LargeLocal>
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: Int,
//    @SerializedName("message") val message: String,
//    @SerializedName("result") val result: Auth?

)