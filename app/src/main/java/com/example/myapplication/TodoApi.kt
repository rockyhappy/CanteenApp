package com.example.myapplication


interface ApiService {
    @POST("api/auth/sign-up/")
    suspend fun fetchData(@Body request: SignUpRequest): Response<SignUpResponse>
}