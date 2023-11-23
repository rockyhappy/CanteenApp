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
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.addCartItemsRequest
import com.example.myapplication.addWishlistRequest
import com.example.myapplication.readFromDataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class ShowItem : Fragment() {
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_show_item, container, false)
        dataStore = requireContext().createDataStore(name = "user")

        val receivedData = arguments?.getString("id")
        Log.d("error",receivedData.toString())
        val mainImage = view.findViewById<ImageView>(R.id.ImageView)
        val heading1= view.findViewById<TextView>(R.id.heading1)
        val price=view.findViewById<TextView>(R.id.price)
        val description = view.findViewById<TextView>(R.id.description)
        val textview1=view.findViewById<TextView>(R.id.textView1)
        val textview2=view.findViewById<TextView>(R.id.textView2)
        val textview3=view.findViewById<TextView>(R.id.textView3)
        lifecycleScope.launch{
            try{
                showCustomProgressDialog()
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getFoodDetail(receivedData.toString())
                Log.d("response",response.toString())
                if(response.isSuccessful)
                {
                    Glide.with(requireContext())
                        .load("https://i.postimg.cc/xTMVqcLJ/Break-fast.png")
                        .apply(
                            RequestOptions()
                                .placeholder(R.drawable.baseline_person_24)
                                .error(R.drawable.baseline_home_24)
                        )
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mainImage)
                    heading1.text=response.body()?.name
                    price.text=response.body()?.price.toString()
                    description.text=response.body()?.description.toString()
                    textview1.text=response.body()!!.ingredients[0].toString()
                    textview2.text=response.body()!!.ingredients[1].toString()
                    textview3.text=response.body()!!.ingredients[2].toString()
                }
                else {
                    showToast("Error")
                    Log.d("Error",response.body().toString())
                }
            }catch(e: Exception){
                showToast("Error")

            }finally {
                showToast("workDone")
                dismissCustomProgressDialog()
            }
        }
        val wish = view.findViewById<ImageView>(R.id.wish)
        wish.setOnClickListener {
            wish.setImageResource(R.drawable.heart_filled)
            lifecycleScope.launch {
                try {
                    showCustomProgressDialog()
                    val email= readFromDataStore(dataStore,"email")
                    val request = addWishlistRequest(
                        userEmail = email.toString(),
                        foodId = receivedData.toString()
                    )
                    val response= RetrofitInstance2.getApiServiceWithToken(dataStore).addWishList(request)
                    if(response.isSuccessful)
                    {
                        Log.d("Testing","Item Added to the list")
                    }
                    else{
                        Log.d("Testing","Failed to add")
                    }
                }catch (e: Exception){
                    Log.d("Testing", "This is catch block")
                }finally{
                    dismissCustomProgressDialog()
                }
            }
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
}