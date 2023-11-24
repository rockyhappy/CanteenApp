package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.FoodItem
import com.example.myapplication.GetFoodByCanteenRequest
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter
import com.example.myapplication.RvAdapter2
import com.example.myapplication.RvModel
import com.example.myapplication.RvModel2
import com.example.myapplication.SpaceItemDecoration
import com.example.myapplication.ViewModel.CanteenMenuViewModel
import com.example.myapplication.addCartItemsRequest
import com.example.myapplication.addToCartRequest
import com.example.myapplication.addWishlistRequest
import com.example.myapplication.readFromDataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ShowCanteenMenu : Fragment() , RvAdapter2.OnItemClickListener , RvAdapter2.OnCartClickListener,RvAdapter2.OnWishClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter2
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    private  val canteenViewModel: CanteenMenuViewModel by  activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataStore = requireContext().createDataStore(name = "user")
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_show_canteen_menu, container, false)
        rvadapter = RvAdapter2(ArrayList(), requireContext(), this,this,this)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvi)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = rvadapter


        val filter= view.findViewById<Button>(R.id.filter)
        filter.setOnClickListener {
            Log.d("Testing","click search")
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, SearchFilter())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val receivedData = arguments?.getString("key").toString()

        val tittle=view.findViewById<TextView>(R.id.tittle)
        tittle.text=receivedData.toString()

        /**
         * This is the code for the back button
         */
        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }


        if (canteenViewModel.canteenItems.value.isNullOrEmpty()) {
            fetchDataFromApi()
        } else {
            updateCanteenRecyclerView(canteenViewModel.canteenItems.value!!)
        }
//        lifecycleScope.launch {
//            try {
//
//                showCustomProgressDialog()
//                val request=GetFoodByCanteenRequest(
//                    name =receivedData.toString()
//                )
//                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteenFood(request)
//                if (response.isSuccessful) {
//                    Log.d("Testing",response.body().toString())
//                    Log.d("Testing", "Successful response: ${response.body()}")
//                    val canteenItems = response.body()?.foodItems.orEmpty()
//
//                    val dataList = canteenItems.map { canteenItem ->
//                        RvModel2(canteenItem.category, canteenItem.name, canteenItem.price.toString(), canteenItem.id)
//                    }
//                    rvadapter.updateData(dataList)
//                } else {
//                    // Handle the error
//                    Log.d("Testing",response.body().toString())
//                    Log.d("Testing", "Response code: ${response.code()}, Response body: ${response.body()}")
//                }
//            } catch (e: Exception) {
//                // Handle network or other exceptions
//                Log.d("Testing","Network Error")
//                Log.e("Testing", "Network Error: ${e.message}", e)
//            }
//            finally {
//                dismissCustomProgressDialog()
//            }
//        }

        return view;
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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



    lifecycleScope.launch {
        try {
            showCustomProgressDialog()
            val email= readFromDataStore(dataStore,"email")
            val request =addWishlistRequest(
                userEmail = email.toString(),
                foodId = name.toString()
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
    private fun fetchDataFromApi() {
        lifecycleScope.launch {
            try {
                showCustomProgressDialog()
                val receivedData = arguments?.getString("key").toString()
                val request = GetFoodByCanteenRequest(
                    name = receivedData
                )
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteenFood(request)
                if (response.isSuccessful) {
                    Log.d("Testing", response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                    val canteenItems = response.body()?.foodItems.orEmpty()

                    // Update ViewModel
                    canteenViewModel.setCanteenItems(canteenItems)
                    updateCanteenRecyclerView(canteenItems)

                } else {
                    Log.d("Testing", response.body().toString())
                    Log.d("Testing", "Response code: ${response.code()}, Response body: ${response.body()}")
                }
            } catch (e: Exception) {
                Log.d("Testing", "Network Error")
                Log.e("Testing", "Network Error: ${e.message}", e)
            } finally {
                dismissCustomProgressDialog()
            }
        }
    }
    private fun updateCanteenRecyclerView(canteenItems: List<FoodItem>) {
        val dataList = canteenItems.map { canteenItem ->
            RvModel2(canteenItem.category, canteenItem.name, canteenItem.price.toString(), canteenItem.id)
        }
        rvadapter.updateData(dataList)
    }

}