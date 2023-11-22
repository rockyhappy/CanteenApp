package com.example.myapplication.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CouponCodeRequest
import com.example.myapplication.DeleteCartItemRequest
import com.example.myapplication.DiscountedPriceResponse
import com.example.myapplication.FoodItem
import com.example.myapplication.FoodItemCart
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter2
import com.example.myapplication.RvAdapterCart
import com.example.myapplication.TotalBillResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.w3c.dom.Text

class cart : Fragment() ,RvAdapterCart.OnDeleteClickListener,RvAdapterCart.OnItemClickListener,
    PaymentResultListener,RvAdapterCart.OnQuantityDecreaseListener,RvAdapterCart.OnQuantityIncreaseListener {
    val check = Checkout()
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapterCart
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Checkout.preload(context)

        // Initialize Razorpay
        Checkout.preload(requireContext())
        check.setKeyID("rzp_test_2sIcIwphd64DGF")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_cart, container, false)
        dataStore = requireContext().createDataStore(name = "user")

        val subTotal = view.findViewById<TextView>(R.id.subtotal)
        val discount=view.findViewById<TextView>(R.id.discount)
        val total =view.findViewById<TextView>(R.id.total)
        rvadapter = RvAdapterCart(ArrayList(), requireContext(), this,this,this,this)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvi)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = rvadapter
        lifecycleScope.launch{
            try{
                showCustomProgressDialog()
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCart()
                if (response.isSuccessful) {
                    val foodItemList: List<FoodItemCart>? = response.body()
                    if (foodItemList != null) {
                        rvadapter.updateData(foodItemList)
                    } else {
                        showToast("Empty or null response body received from the server.")
                    }
                } else {
                    showToast("Failed to retrieve data. Code: ${response.code()}")
                }
            }catch (e: Exception)
            {
                showToast("Catch Block")
            }finally{
                dismissCustomProgressDialog()
                try {
                    showCustomProgressDialog()
                    val bill=RetrofitInstance2.getApiServiceWithToken(dataStore).getTotalBill()
                    subTotal.text=bill.toString()
                }catch (e: Exception){
                    showToast("Connection Error")
                }finally {
                    dismissCustomProgressDialog()
                }


            }

        }

        Checkout.preload(requireContext())


        val cart=view.findViewById<Button>(R.id.cart)
        cart.setOnClickListener {
            showToast("Redirecting To Payment")
            startRazorpayPayment()
        }

        val add = view.findViewById<TextView>(R.id.add)
        add.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, Dishes_Category())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val apply = view.findViewById<Button>(R.id.apply)
        apply.setOnClickListener {
            showApplyDialog()
        }
        return view
    }

    override fun onPlusClick(name: Long) {

    }

    override fun onMinusClick(name: Long) {

    }


    override fun onItemClick(name: Long) {
        //this is the code for the show Item
        val bundle =Bundle()
        bundle.putString("id",name.toString())
        val passing =ShowItem()
        passing.arguments=bundle
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment, passing)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        //showToast(name.toString())
    }

    override fun onDeleteClick(name: Long) {
        val request = DeleteCartItemRequest(cartItemId = name.toString())
        lifecycleScope.launch {
            try {
                showCustomProgressDialog()

                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).deleteCartItem(request)

                if (response.isSuccessful) {
                    showToast("Item deleted successfully")
                    val position = rvadapter.dataList.indexOfFirst{ it.id == name }
                    rvadapter.removeItem(position)
                    rvadapter.updateData(rvadapter.dataList)
                } else {
                    showToast("Failed to delete item. Code: ${response.code()}")
                }
            } catch (e: Exception) {
                showToast("An error occurred: ${e.message}")
            } finally {
                dismissCustomProgressDialog()
                //now the item has been deleted but the datalist is not updated
                try{
                    showCustomProgressDialog()
                    val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCart()
                    if (response.isSuccessful) {
                        val foodItemList: List<FoodItemCart>? = response.body()
                        if (foodItemList != null) {
                            rvadapter.updateData(foodItemList)
                        } else {
                            showToast("Empty or null response body received from the server.")
                        }
                    } else {
                        showToast("Failed to retrieve data. Code: ${response.code()}")
                    }
                }catch (e: Exception)
                {
                    showToast("Catch Block")
                }finally{
                    dismissCustomProgressDialog()
                    try {
                        showCustomProgressDialog()
                        val bill=RetrofitInstance2.getApiServiceWithToken(dataStore).getTotalBill()
                        val subTotal = view?.findViewById<TextView>(R.id.subtotal)
                        subTotal?.text=bill.toString()
                    }catch (e: Exception){
                        showToast("Connection Error")
                    }finally {
                        dismissCustomProgressDialog()
                    }
                }
            }
        }
    }

    private fun loadCart()
    {
        lifecycleScope.launch{
            try{
                showCustomProgressDialog()
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCart()
                if (response.isSuccessful) {
                    val foodItemList: List<FoodItemCart>? = response.body()
                    if (foodItemList != null) {
                        rvadapter.updateData(foodItemList)
                    } else {
                        showToast("Empty or null response body received from the server.")
                    }
                } else {
                    showToast("Failed to retrieve data. Code: ${response.code()}")
                }
            }catch (e: Exception)
            {
                showToast("Catch Block")
            }finally{
                dismissCustomProgressDialog()
                //now the code for the total bill
                getTotalBill()

            }


            // now for the total price of the cart
            val total = view?.findViewById<TextView>(R.id.total)
            total?.text=getTotalBill().toString()
            val subTotal = view?.findViewById<TextView>(R.id.subtotal)
            subTotal?.text=total?.text

        }

    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showCustomProgressDialog() {
        dialog = Dialog(requireContext())
        dialog?.setContentView(R.layout.custom_dialog_loading)
        dialog?.setCancelable(false)

        val window = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog?.show()
    }
    private fun dismissCustomProgressDialog() {
        dialog?.dismiss()
        dialog = null
    }


    private fun showApplyDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog__layout, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val closeImageView = dialogView.findViewById<ImageView>(R.id.closeImageView)
        val editTextCouponCode = dialogView.findViewById<EditText>(R.id.editText)
        val yesButton = dialogView.findViewById<Button>(R.id.yesButton)
        val noButton = dialogView.findViewById<Button>(R.id.noButton)

        closeImageView.setOnClickListener {
            alertDialog.dismiss()
        }

        yesButton.setOnClickListener {

            showToast("Yes clicked, Input: ${editTextCouponCode.text}")
            alertDialog.dismiss()
            val couponCode = editTextCouponCode.text.toString()

// Create an instance of CouponCodeRequest
            val couponCodeRequest = CouponCodeRequest(couponCode = couponCode)

            lifecycleScope.launch {
                try {
                    showCustomProgressDialog()

                    val response = RetrofitInstance2.getApiServiceWithToken(dataStore).calculateDiscountedPrice(couponCodeRequest )

                    if (response.isSuccessful) {
                        val discountedPriceResponse: DiscountedPriceResponse? = response.body()

                        if (discountedPriceResponse != null) {
                            // Now you have the 'discountedPriceResponse', you can use its properties.
                            val discountedPrice = discountedPriceResponse.discountedPrice
                            showToast(discountedPrice.toString())
                            val total = view?.findViewById<TextView>(R.id.total)
                            val discount = view?.findViewById<TextView>(R.id.discount)
                            val subTotal = view?.findViewById<TextView>(R.id.subtotal)
                            total?.text=discountedPrice.toString()
                            discount?.text=(subTotal?.text.toString().toInt()-total?.text.toString().toInt()).toString()
                            // Handle the discounted price as needed.
                        } else {
                            showToast("Empty or null response body received from the server.")
                        }
                    } else {
                        showToast("Failed to retrieve data. Code: ${response.code()}")
                    }
                } catch (e: Exception) {
                    showToast("An error occurred: ${e.message}")
                } finally {
                    dismissCustomProgressDialog()
                }
            }

        }

        noButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    suspend fun getTotalBill(): Double? {
        return try {
            // Show your progress dialog if needed
            showCustomProgressDialog()
            val total = view?.findViewById<TextView>(R.id.total)
            val response = RetrofitInstance2.getApiServiceWithToken(dataStore)
                .getTotalBill()
            total?.text=response.toString()



//            if (response.isSuccessful) {
//                val totalBillResponse: TotalBillResponse? = response.body()
//
//                if (totalBillResponse != null) {
//                    // Now you have the 'totalBill', you can use it as needed.
//                    val totalBill = totalBillResponse.totalBill
//                    total?.text=totalBill.toString()
//                    showToast("Total Bill: $totalBill")
//                    totalBill
//                } else {
//                    showToast("Empty or null response body received from the server.")
//                    null
//                }
//            } else {
//                showToast("Failed to get total bill. Code: ${response.code()}")
//                null
//            }
            null
        } catch (e: Exception) {
            showToast("An error occurred: ${e.message}")
            null
        } finally {
            // Dismiss your progress dialog if needed
            dismissCustomProgressDialog()
        }
    }


    /**
     * This is to implement RazorPay
     */
    private fun startRazorpayPayment() {
        // Create a JSONObject with payment details
        val options = JSONObject()
        options.put("name", "BrunchBliss")
        options.put("description", "Payment for items in the cart")
        options.put("image", "YOUR_APP_LOGO_URL")
        options.put("currency", "INR")
        options.put("amount", "100000")  // Amount in paise (80 rupees * 100)
        options.put("theme.color", "#FFFA902D")  // Optional, set the theme color

        // Open Razorpay checkout activity
        try {
            check.open(requireActivity(), options)
        } catch (e: Exception) {
            // Handle exception
            showToast("Error in payment initiation")
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {

        Log.d("RazorPay",razorpayPaymentId.toString())
        showToast("$razorpayPaymentId")
        Handler().postDelayed({
            showToast("$razorpayPaymentId")
        }, 3000)
    }

    override fun onPaymentError(code: Int, response: String?) {
        // Handle payment failure
        showToast("Payment Failed. Code: $code, Response: $response")
    }

}