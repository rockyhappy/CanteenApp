package com.example.myapplication.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class cart : Fragment() ,RvAdapterCart.OnDeleteClickListener,RvAdapterCart.OnItemClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapterCart
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_cart, container, false)
        dataStore = requireContext().createDataStore(name = "user")

        rvadapter = RvAdapterCart(ArrayList(), requireContext(), this,this)
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
            }
        }


        val cart=view.findViewById<Button>(R.id.cart)
        cart.setOnClickListener {
            showToast("Razor Pay")
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
    override fun onItemClick(name: Long) {
//        val bundle =Bundle()
//        bundle.putString("id",name.toString())
//        val passing =ShowItem()
//        passing.arguments=bundle
//        val fragmentTransaction = parentFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.flFragment, passing)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
//        showToast(name.toString())
    }

    override fun onDeleteClick(name: Long) {

        Log.d("deleting","inprogress")
        val request = DeleteCartItemRequest(cartItemId = name.toString())
        lifecycleScope.launch {
            try {
                // Show your progress dialog if needed
                showCustomProgressDialog()
                val response =RetrofitInstance2.getApiServiceWithToken(dataStore).deleteCartItem(request)

                if (response.isSuccessful) {
                    // Handle success, refresh the data, etc.
                    showToast("Item deleted successfully")
                } else {
                    // Handle API error
                    showToast("Failed to delete item. Code: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle exception
                showToast("An error occurred: ${e.message}")
            } finally {
                // Dismiss your progress dialog if needed
                dismissCustomProgressDialog()
            }
        }
    }
//    private fun deleteCartItem(itemId: Long) {
//        lifecycleScope.launch {
//            try {
//                // Show your progress dialog if needed
//
//                val response =
//                    RetrofitInstance2.getApiServiceWithToken(dataStore).deleteCartItem(itemId)
//
//                if (response.isSuccessful) {
//                    // Handle success, refresh the data, etc.
//                    showToast("Item deleted successfully")
//                } else {
//                    // Handle API error
//                    showToast("Failed to delete item. Code: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                // Handle exception
//                showToast("An error occurred: ${e.message}")
//            } finally {
//                // Dismiss your progress dialog if needed
//            }
//        }
//    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
            // Handle No button click
            showToast("No clicked")
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}