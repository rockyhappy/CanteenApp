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

data class RvModel(var canteenUrl : String , var name: String , var descriptionn :String)


/**
 * Data classes for the Dashboard
 */
data class CanteenResponse(
    val canteenItems: List<CanteenItem>
)

data class CanteenItem(
    val id: Long,
    val name: String,
    val email: String,
    val description: String,
    val canteenImage: String,
    val foods: List<Int>
)

data class GetFoodByCanteenRequest(
    val name :String
)
data class GetFoodByCanteenResponse(
    val foodItems: List<FoodItem>
)
data class FoodItem(
    val id: Long,
    val name: String,
    val category: String,
    val price: Double,
    val canteenId: Long,
    val foodImage: String,
    val description: String
)
