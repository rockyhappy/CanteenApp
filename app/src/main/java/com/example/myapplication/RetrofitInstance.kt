package com.example.myapplication
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * This is the retrofit instance for the authentication
 */
object RetrofitInstance {
    private const val BASE_URL = "https://brunchbliss.onrender.com/"
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}


/**
 * this is the interceptor to add the jwt to the header of the request
 */
class AuthInterceptor(private val jwtToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer $jwtToken")
        val request = requestBuilder.build()
        Log.d("JWT_TOKEN", jwtToken)
        return chain.proceed(request)
    }
}




/**
 * These are the retrofit instance for the jwt Authentication
 */

object RetrofitInstance2 {
    private const val BASE_URL = "https://brunchbliss.onrender.com"

    private fun createApiService(jwtToken: String): ApiService {
        val authInterceptor = AuthInterceptor(jwtToken)
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    // Function to get the JWT token from DataStore
    suspend fun getApiServiceWithToken(dataStore: DataStore<Preferences>): ApiService {
        //val jwtToken = readFromDataStore(dataStore, "token").toString()
        val jwtToken="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyYWNAZ21haWwuY29tIiwiaWF0IjoxNjk5OTkwMTgzLCJleHAiOjE3MDA1MTU3ODN9.uSNOwME1rmo2_4sxeMV3iBwhAvmMjNpYcypIaU6X8oo"
        return createApiService(jwtToken)
    }
}
