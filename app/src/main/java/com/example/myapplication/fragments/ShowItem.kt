package com.example.myapplication.fragments

import android.app.Dialog
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.FoodItem
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.ViewModel.FoodItemViewModel
import com.example.myapplication.addCartItemsRequest
import com.example.myapplication.addWishlistRequest
import com.example.myapplication.deleteFromWishlistRequest
import com.example.myapplication.readFromDataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class ShowItem : Fragment() {
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    private  val foodItemViewModel: FoodItemViewModel  by  activityViewModels()
    private lateinit var progressBar: ProgressBar
    private  var isInWish: Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_show_item, container, false)
        dataStore = requireContext().createDataStore(name = "user")
        progressBar=view.findViewById(R.id.progressBar)
        val receivedData = arguments?.getString("id")
        Log.d("error",receivedData.toString())
        val mainImage = view.findViewById<ImageView>(R.id.ImageView)
        val heading1= view.findViewById<TextView>(R.id.heading1)
        val price=view.findViewById<TextView>(R.id.price)
        val description = view.findViewById<TextView>(R.id.description)
        val textview1=view.findViewById<TextView>(R.id.textView1)
        val textview2=view.findViewById<TextView>(R.id.textView2)
        val textview3=view.findViewById<TextView>(R.id.textView3)

        val foodId = receivedData?.toLongOrNull()

        if (foodId != null) {
            val cachedFoodItem = foodItemViewModel.getFoodItem(foodId)
            if (cachedFoodItem != null) {
                updateUI(view,cachedFoodItem)
            } else {
                fetchDataFromApi(foodId)
            }
        }


        val wish = view.findViewById<ImageView>(R.id.wish)
        wish.setOnClickListener {
            if(!isInWish)
            {
                lifecycleScope.launch {
                    try {
                        //showCustomProgressDialog()
                        val email= readFromDataStore(dataStore,"email")
                        val request =addWishlistRequest(
                            userEmail = email.toString(),
                            foodId = foodId.toString()
                        )
                        val response= RetrofitInstance2.getApiServiceWithToken(dataStore).addWishList(request)
                        if(response.isSuccessful)
                        {
                            Log.d("Testing","Item Added to the list")
                            showToast(response.body()?.message.toString())
                        }
                        else{
                            Log.d("Testing","Failed to add")
                        }
                    }catch (e: Exception){
                        Log.d("Testing", "This is catch block")
                    }finally{
                        //dismissCustomProgressDialog()
                    }
                }
            }
            else{
                lifecycleScope.launch{
                    try{
                        //showCustomProgressDialog()
                        val email= readFromDataStore(dataStore,"email")
                        val request= deleteFromWishlistRequest(
                            email= email.toString(),
                            foodId = foodId.toString()
                        )
                        val response = RetrofitInstance2.getApiServiceWithToken(dataStore).deleteFromWishlist(request)
                        if(response.isSuccessful)
                        {
                            showToast(response.body()?.message.toString())
                            Log.d("ResponseCart",response.body()?.message.toString())
                        }

                    }catch (e:Exception){
                        showToast("Connection Error")
                    }finally{
                        //dismissCustomProgressDialog()
                        //wishlistReload()
                    }
                }
            }
            wish.setImageResource(R.drawable.heart_filled)
        }



        val cart = view.findViewById<Button>(R.id.cart)
        cart.setOnClickListener {
            lifecycleScope.launch{
                try {
                    showCustomProgressDialog()
                    val request = addCartItemsRequest(
                        foodId = receivedData!!.toLong(),
                        quantity = "1"
                    )
                    val response = RetrofitInstance2.getApiServiceWithToken(dataStore).addCartItems(request)
                    Log.d("response",response.toString())
                    if(response.isSuccessful){
                        showToast("item Added")
                    }else{
                        showToast("Failed to add")
                    }
                }catch (e:Exception){
                        showToast("Error occured")
                }finally {
                    showToast("this is finally block")
                    dismissCustomProgressDialog()
                }
            }
        }

        /**
         * This is the code for the back button
         */
        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            parentFragmentManager.popBackStack()

        }



        return view
    }

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
    private fun fetchDataFromApi(foodId: Long) {
        lifecycleScope.launch {
            try {
                //showCustomProgressDialog()
                showProgressBar()
                //progressBar.visibility = View.VISIBLE
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getFoodDetail(foodId.toString())
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    val foodItem = response.body()
                    if (foodItem != null) {
                        // Update ViewModel
                        foodItemViewModel.setFoodItem(foodId, foodItem)
                        updateUI(requireView(),foodItem)
                    } else {
                        showToast("Error")
                        Log.d("Error", response.body().toString())
                    }
                } else {
                    showToast("Error")
                    Log.d("Error", response.body().toString())
                }
            } catch (e: Exception) {
                showToast("Error")
                Log.d("Error", "Network Error: ${e.message}", e)
            } finally {
                //dismissCustomProgressDialog()
                hideProgressBar()
            }
        }
    }
    private fun updateUI(view: View,foodItem: FoodItem) {
        var mainImage = view.findViewById<ImageView>(R.id.ImageView)
        Glide.with(requireContext())
            .load("https://i.postimg.cc/xTMVqcLJ/Break-fast.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_home_24)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mainImage)


        val heading1= view.findViewById<TextView>(R.id.heading1)
        val price=view.findViewById<TextView>(R.id.price)
        val description = view.findViewById<TextView>(R.id.description)
        val textview1=view.findViewById<TextView>(R.id.textView1)
        val textview2=view.findViewById<TextView>(R.id.textView2)
        val textview3=view.findViewById<TextView>(R.id.textView3)
        heading1.text = foodItem.name
        price?.text = foodItem.price.toString()
        description?.text = foodItem.description
        isInWish=foodItem.isInWishlist
        if(isInWish)
        {
            requireView().findViewById<ImageView>(R.id.wish).setImageResource(R.drawable.heart_filled)
        }
        else
        {
            requireView().findViewById<ImageView>(R.id.wish).setImageResource(R.drawable.heart)
        }

        textview1?.text = foodItem.ingredients.getOrNull(0) ?: ""
        textview2?.text = foodItem.ingredients.getOrNull(1) ?: ""
        textview3?.text = foodItem.ingredients.getOrNull(2) ?: ""

    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
//    private fun hideOtherWidgets() {
//        requireView().findViewById<HorizontalScrollView>(R.id.scroll).visibility=View.GONE
//        requireView().findViewById<ImageView>(R.id.imageView7)?.visibility = View.GONE
//        requireView().findViewById<RecyclerView>(R.id.rvid).visibility=View.GONE
//        requireView().findViewById<TextView>(R.id.canteen).visibility=View.GONE
//        requireView().findViewById<TextView>(R.id.fullCategory).visibility=View.GONE
//        requireView().findViewById<TextView>(R.id.category).visibility=View.GONE
//        requireView().findViewById<TextView>(R.id.textView10).visibility=View.GONE
//    }
//    private fun showOtherWidgets() {
//        requireView().findViewById<HorizontalScrollView>(R.id.scroll).visibility = View.VISIBLE
//        requireView().findViewById<ImageView>(R.id.imageView7)?.visibility = View.VISIBLE
//        requireView().findViewById<RecyclerView>(R.id.rvid).visibility = View.VISIBLE
//        requireView().findViewById<TextView>(R.id.canteen).visibility=View.VISIBLE
//        requireView().findViewById<TextView>(R.id.fullCategory).visibility=View.VISIBLE
//        requireView().findViewById<TextView>(R.id.category).visibility=View.VISIBLE
//        requireView().findViewById<TextView>(R.id.textView10).visibility=View.VISIBLE
//    }
}