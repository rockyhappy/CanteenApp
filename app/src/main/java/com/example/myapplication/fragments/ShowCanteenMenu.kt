package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
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
import com.example.myapplication.deleteFromWishlistRequest
import com.example.myapplication.readFromDataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShowCanteenMenu : Fragment() , RvAdapter2.OnItemClickListener , RvAdapter2.OnCartClickListener,RvAdapter2.OnWishClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter2
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    private  val canteenViewModel: CanteenMenuViewModel by  activityViewModels()
    private lateinit var progressBar: ProgressBar
    private var apiCallJob: Job? = null
    private var apiCallJobWish: Job? = null
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
        progressBar = view.findViewById(R.id.progressBar)
        if (canteenViewModel.getCanteenItems(receivedData).isNullOrEmpty()) {
            showProgressBar()
            fetchDataFromApi()
        } else {
            hideProgressBar()
            updateCanteenRecyclerView(canteenViewModel.getCanteenItems(receivedData)!!)
        }

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

    override fun onCartClick(name: Long, isIn: Boolean) {
        apiCallJob?.cancel()
        if(isIn==false)
        {
            apiCallJob?.cancel()
            apiCallJob = lifecycleScope.launch {
                try{
                    //showCustomProgressDialog()
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
                    //showToast("Connection Error")
                }finally{
                    //dismissCustomProgressDialog()
                }


            }
        }
        else
        {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, cart())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    override fun onWishClick(name: Long, isIn: Boolean) {
        apiCallJobWish?.cancel()
        if(!isIn)
        {
            lifecycleScope.launch {
                try {
                    //showCustomProgressDialog()
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
                    //dismissCustomProgressDialog()
                    //wishlistReload()
                }
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
                //showCustomProgressDialog()
                val receivedData = arguments?.getString("key").toString()
                val request = GetFoodByCanteenRequest(
                    name = receivedData
                )
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteenFood(request)
                if (response.isSuccessful) {
                    Log.d("Testing", response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                    val canteenItems = response.body()?.foodItems.orEmpty()
                    canteenViewModel.setCanteenItems(receivedData, canteenItems)
                    updateCanteenRecyclerView(canteenItems)

                } else {
                    Log.d("Testing", response.body().toString())
                    Log.d("Testing", "Response code: ${response.code()}, Response body: ${response.body()}")
                }
            } catch (e: Exception) {
                Log.d("Testing", "Network Error")
                Log.e("Testing", "Network Error: ${e.message}", e)
            } finally {
                //dismissCustomProgressDialog()
                hideProgressBar()
            }
        }
    }
    private fun updateCanteenRecyclerView(canteenItems: List<FoodItem>) {
        val dataList = canteenItems.map { canteenItem ->
            FoodItem(
                id = canteenItem.id,
                name = canteenItem.name,
                category = canteenItem.category,
                price = canteenItem.price,
                canteenId = canteenItem.canteenId,
                foodImage = canteenItem.foodImage,
                description = canteenItem.description,
                averageRating = canteenItem.averageRating,
                isInWishlist = canteenItem.isInWishlist,
                isInCart = canteenItem.isInCart,
                noOfRatings = canteenItem.noOfRatings,
                veg = canteenItem.veg,
                ingredients = canteenItem.ingredients,
                ingredientImageList = canteenItem.ingredientImageList
            )

        }
        rvadapter.updateData(dataList)
    }


    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}