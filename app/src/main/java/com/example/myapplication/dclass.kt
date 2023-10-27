package com.example.myapplication


data class SignUpResponse(
   val success: String,
    val message:String
)

data class LoginResponse(
    val token: String
)
data class UserData(
    val username: String,
    val password: String,
    val email: String
)
data class SignUpRequest(
    val username: String,
    val name: String,
    val email: String,
    val password: String
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class verifyMailRequest(
    val email :String,
    val otp: String
)
data class verifyMailResponse(
    val success:String,
    val message : String
)