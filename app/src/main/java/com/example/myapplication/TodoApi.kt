package com.example.myapplication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("sign-up")
    suspend fun fetchData(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("api/v1/auth/login")
    suspend fun fetchLoginData(@Body request : LoginRequest): Response<LoginResponse>

    @POST("verify-email")
    suspend fun checkEmail(@Body request: verifyMailRequest) : Response<verifyMailResponse>
}