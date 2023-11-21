package com.example.myapplication.fragments

import RvAdapterWishlist
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.FoodItemCart
import com.example.myapplication.FoodItemWishlist
import com.example.myapplication.GetFoodByCanteenRequest
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter2
import com.example.myapplication.RvModel2
import com.example.myapplication.addCartItemsRequest
import com.example.myapplication.deleteFromWishlistRequest
import com.example.myapplication.getWishlistRequest
import com.example.myapplication.readFromDataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class wishlist : Fragment() ,RvAdapterWishlist.OnCartClickListener,RvAdapterWishlist.OnItemClickListener,RvAdapterWishlist.OnWishClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapterWishlist
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataStore = requireContext().createDataStore(name = "user")
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_wishlist, container, false)
        rvadapter = RvAdapterWishlist(ArrayList(), requireContext(), this,this,this)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvi)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = rvadapter

        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
        lifecycleScope.launch {
            try {

                showCustomProgressDialog()

                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getWishlist()
                if (response.isSuccessful) {
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                    val foodItemList: List<FoodItemWishlist>? = response.body()
                    if (foodItemList != null) {
                        rvadapter.updateData(foodItemList)
                    } else {
                        showToast("Empty or null response body received from the server.")
                    }
                    //rvadapter.updateData(foodItemList)
                } else {
                    // Handle the error
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Response code: ${response.code()}, Response body: ${response.body()}")
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.d("Testing","Network Error")
                Log.e("Testing", "Network Error: ${e.message}", e)
            }
            finally {
                dismissCustomProgressDialog()
            }
        }

        return view
    }

    override fun onCartClick(name: Long) {
        lifecycleScope.launch {
            try{
                showCustomProgressDialog()
                val request= addCartItemsRequest(
                    foodId = name,
                    quantity = "1"
                )
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).addCartItems(request)
                if(response.isSuccessful)
                {
                    showToast(response.body()?.message.toString())
                    Log.d("ResponseCart",response.body()?.message.toString())
                }

            }catch (e:Exception){
                showToast("Connection Error")
            }finally{
                dismissCustomProgressDialog()
            }


        }
    }

    override fun onWishClick(name: Long) {
        lifecycleScope.launch{
            try{
                showCustomProgressDialog()
                val email= readFromDataStore(dataStore,"email")
                val request= deleteFromWishlistRequest(
                    email= email.toString(),
                    foodId = name.toString()
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
                dismissCustomProgressDialog()
                wishlistReload()
            }
        }
    }

    fun wishlistReload(){
        lifecycleScope.launch {
            try {

                showCustomProgressDialog()

                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getWishlist()
                if (response.isSuccessful) {
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                    val foodItemList: List<FoodItemWishlist>? = response.body()
                    if (foodItemList != null) {
                        rvadapter.updateData(foodItemList)
                    } else {
                        showToast("Empty or null response body received from the server.")
                    }
                    //rvadapter.updateData(foodItemList)
                } else {
                    // Handle the error
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Response code: ${response.code()}, Response body: ${response.body()}")
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.d("Testing","Network Error")
                Log.e("Testing", "Network Error: ${e.message}", e)
            }
            finally {
                dismissCustomProgressDialog()
            }
        }
    }
    override fun onItemClick(name: Long) {
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
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}