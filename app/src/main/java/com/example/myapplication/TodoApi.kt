package com.example.myapplication
import com.example.myapplication.fragments.ForgotPassward
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("api/v1/auth/reset-new-password")
    suspend fun ResetPassword(@Body request: ResetPasswordRequest) : Response<ResetPasswordResponse>
    @POST("api/v1/auth/reset-password-verify")
    suspend fun resetPasswordCheckEmail(@Body request: verifyMailRequest) : Response<verifyMailResponse>

    @GET("api/v1/user/get-canteens")
    suspend fun getCanteens() : Response<CanteenResponse>

    @POST("api/v1/user/get-canteen-food")
    suspend fun getCanteenFood(@Body request: GetFoodByCanteenRequest) : Response<GetFoodByCanteenResponse>
    @POST("api/v1/user/get-food-items")
    suspend fun getCategoryFood(@Body request: GetFoodByCategoryRequest) : Response<GetFoodByCanteenResponse>

    @GET("api/v1/user/food/{id}")
    suspend fun getFoodDetail(@Path("id")id:String) : Response<FoodItem>

    @POST("api/v1/cart/add-to-cart")
    suspend fun addCartItems(@Body request: addCartItemsRequest) : Response<addCartItemsResponse>

    @GET("api/v1/cart/get-cart-items")
    suspend fun getCart() :Response<List<FoodItemCart>>

    @POST("api/v1/cart/calculateDiscountedPrice")
    suspend fun calculateDiscountedPrice(@Body couponCodeRequest: CouponCodeRequest): Response<DiscountedPriceResponse>

    @POST("api/v1/cart/delete-cart-item")
    suspend fun deleteCartItem(@Body request: DeleteCartItemRequest): Response<DeleteCartItemResponse>

    @GET("api/v1/cart/total-bill")
    suspend fun getTotalBill(): Double

    @POST("api/v1/wishlist/add")
    suspend fun addWishList(@Body request : addWishlistRequest) : Response<addWishlistResponse>

    @GET("api/v1/wishlist/get")
    suspend fun getWishlist() : Response<List<FoodItemWishlist>>

    @POST("api/v1/wishlist/delete")
    suspend fun deleteFromWishlist(@Body request:deleteFromWishlistRequest) :Response<addWishlistResponse>


    @POST("api/v1/payments/create-order")
    suspend fun paymentCreateOrder(@Body request: PaymentInfo) : Response<Order>

    @POST("api/v1/payments/capture-payment")
    suspend fun capturePayment(@Body request: PaymentInfo2) :Response<Payment>


    @FormUrlEncoded
    @POST("api/v1/user/search")
    fun submitFormData(
        @Field("canteenId") canteenId: Long?,
        @Field("foodName") foodName: String?,
        @Field("category") category: String?,
        @Field("lowPrice") lowPrice: Double?,
        @Field("highPrice") highPrice: Double?,
        @Field("veg") veg: Boolean?,
        @Field("rating") rating: Double?,
    ): Call<List<FoodItem>>

    @GET("api/v1/qrCode/generate")
    suspend fun qrGenerate() : String?

    @POST("api/v1/qrCode/validate")
    suspend fun qrScanner(@Body request :addCartItemsResponse): String
}
