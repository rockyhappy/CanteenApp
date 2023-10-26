package com.example.myapplication


data class SignUpResponse(
    val success: Boolean,
    val message: String,
    val data: UserData
)

data class UserData(
    val username: String,
    val password: String,
    val email: String
)
data class SignUpRequest(
    val username: String,
    val password: String,
    val email: String
)
