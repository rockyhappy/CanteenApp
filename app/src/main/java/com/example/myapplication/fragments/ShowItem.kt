package com.example.myapplication.fragments

import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.addCartItemsRequest
import kotlinx.coroutines.launch


class ShowItem : Fragment() {
    private lateinit var dataStore: DataStore<Preferences>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_show_item, container, false)
        dataStore = requireContext().createDataStore(name = "user")

        val receivedData = arguments?.getString("id")
        Log.d("error",receivedData.toString())

        lifecycleScope.launch{
            try{
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getFoodDetail(receivedData.toString())
                Log.d("response",response.toString())
                if(response.isSuccessful)
                {

                }
                else {
                    showToast("Error")
                    Log.d("Error",response.body().toString())
                }
            }catch(e: Exception){
                showToast("Error")

            }finally {
                showToast("workDone")
            }
        }
        val wish = view.findViewById<ImageView>(R.id.wish)
        wish.setOnClickListener {
            wish.setImageResource(R.drawable.heart_filled)
            //add to wish list api work
        }
        val cart = view.findViewById<Button>(R.id.cart)
        cart.setOnClickListener {
            lifecycleScope.launch{
                try {
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
                }
            }
        }


        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}