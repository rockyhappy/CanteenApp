package com.example.myapplication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/sign-up/")
    suspend fun fetchData(@Body request: SignUpRequest): Response<SignUpResponse>
}