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

data class RvModel2(var canteenUrl : String , var name: String , var price :String , var id : Long)


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

data class GetFoodByCategoryRequest(
    val category: String
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
    val description: String,
    val averageRating: Double,
    val isInWishlist: Boolean,
    val isInCart: Boolean,
    val ingredients: List<String>,
    val ingredientImageList: List<String>
)

data class addCartItemsRequest(
    val foodId: Long,
    val quantity: String
)
data class addCartItemsResponse(
    val message:String

)

data class getCartResponse(
    val id: Long,
    val foodItemName: String,
    val quantity: Int,
    val price: Double
)

data class addToCartRequest(
    val foodId: Long,
    val quantity: String
)

data class FoodItemCart(
    val id: Long,
    val foodItemName: String,
    val quantity: Int,
    val price: Double,
    val canteenId: Long
)

data class DiscountedPriceResponse(
    val discountedPrice: Double,
    val errorMessage: String?
)
data class CouponCodeRequest(
    val couponCode: String
)
data class DeleteCartItemResponse(
    val message: String?
)
data class DeleteCartItemRequest(
    val cartItemId: String
)
data class TotalBillResponse(
    val totalBill: Double
)

data class addWishlistRequest(
    val userEmail:String,
    val foodId : String
)

data class addWishlistResponse(
    val message: String
)

data class getWishlistRequest(
    val email: String
)

data class FoodItemWishlist(
    val id: Long,
    val name: String,
    val category: String,
    val price: Double,
    val canteenId: Long,
    val foodImage: String,
    val description: String,
    val ingredients: List<String>,
    val ingredientImageList: List<String>
)


data class deleteFromWishlistRequest(
    val email :String,
    val foodId :String
)



data class Order(
    val amount: Int,
    val amount_paid: Int,
    val notes: List<String>,
    val created_at: Long,
    val amount_due: Int,
    val currency: String,
    val receipt: String,
    val id: String,
    val entity: String,
    val offer_id: String?,
    val status: String,
    val attempts: Int
)
data class PaymentInfo(
    val amount: Double,
    val currency: String,
    val receipt: String
)

data class Payment(
    val notes: List<String>,
    val fee: Int,
    val description: String,
    val created_at: Long,
    val amount_refunded: Int,
    val bank: String,
    val error_reason: String?, // Nullable since it can be null in the JSON
    val error_description: String?, // Nullable since it can be null in the JSON
    val acquirer_data: AcquirerData,
    val captured: Boolean,
    val contact: String,
    val invoice_id: String?, // Nullable since it can be null in the JSON
    val currency: String,
    val id: String,
    val international: Boolean,
    val email: String,
    val amount: Int,
    val refund_status: String?, // Nullable since it can be null in the JSON
    val wallet: String?, // Nullable since it can be null in the JSON
    val method: String,
    val vpa: String?, // Nullable since it can be null in the JSON
    val error_source: String?, // Nullable since it can be null in the JSON
    val error_step: String?, // Nullable since it can be null in the JSON
    val tax: Int,
    val card_id: String?, // Nullable since it can be null in the JSON
    val error_code: String?, // Nullable since it can be null in the JSON
    val order_id: String?, // Nullable since it can be null in the JSON
    val entity: String,
    val status: String
)

data class AcquirerData(
    val bank_transaction_id: String
)


data class PaymentInfo2(
    val paymentId: String,
    val amount: Double
)
