package com.example.myapplication
import com.example.myapplication.fragments.ForgotPassward
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/v1/auth/register")
    suspend fun fetchData(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("api/v1/auth/login")
    suspend fun fetchLoginData(@Body request : LoginRequest): Response<LoginResponse>

    @POST("api/v1/auth/verify-email")
    suspend fun checkEmail(@Body request: verifyMailRequest) : Response<verifyMailResponse>

    @POST("api/v1/auth/resend-otp")
    suspend fun resendOtp(@Body request: resendOtpRequest) : Response<resendOtpResponse>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request : LoginRequest) : Response<LoginResponse>

    @POST("api/v1/auth/forgot-password")
    suspend fun ForgotPassward(@Body request:forgotPasswordRequest) : Response<forgotPasswordResponse>

}