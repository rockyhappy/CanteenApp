package com.example.myapplication


data class SignUpResponse(
    val token:String,
    val message:String
)
data class SignUpRequest(
    val fullName:String,
    val email:String,
    val password:String,
    val role:String
)
data class LoginResponse(
    val token: String,
    val message: String
)

data class LoginRequest(
    val email: String,
    val password: String
)
data class UserData(
    val username: String,
    val password: String,
    val email: String
)

data class verifyMailRequest(
    val email :String,
    val otp: String
)

data class verifyMailResponse(
    val token:String,
    val message: String
)

data class resendOtpRequest(
    val email:String
)

data class resendOtpResponse(
    val token:String,
    val message: String
)

data class forgotPasswordRequest(
    val email:String
)

data class forgotPasswordResponse(
    val token:String
)

data class ResetPasswordRequest(
    val email:String,
    val newPassword:String
)
data class ResetPasswordResponse(
    val token:String
)

data class RvModel(var profile :Int , var name: String , var adress :String) {
}