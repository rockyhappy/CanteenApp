package com.example.myapplication.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter
import com.example.myapplication.RvModel
import kotlinx.coroutines.launch
import android.speech.RecognizerIntent
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.CanteenItem
import com.example.myapplication.ViewModel.SharedViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.FoodItem
import com.example.myapplication.PaymentInfo2
import com.example.myapplication.RvAdapter2
import com.example.myapplication.addCartItemsRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import javax.security.auth.callback.Callback


class MainDashboard : Fragment(R.layout.fragment_main_dashboard) , RvAdapter.OnItemClickListener , RvAdapter2.OnItemClickListener,RvAdapter2.OnCartClickListener,RvAdapter2.OnWishClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    private  val sharedViewModel: SharedViewModel  by activityViewModels()
    private lateinit var progressBar: ProgressBar
    private var searchJob: Job? = null
    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var rvadapterSearch : RvAdapter2
    private lateinit var searchProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataStore = requireContext().createDataStore(name = "user")
        val view= inflater.inflate(R.layout.fragment_main_dashboard, container, false)
        rvadapter = RvAdapter(ArrayList(), requireContext(), this)
        recyclerView = view.findViewById(R.id.rvid)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = rvadapter
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        if (sharedViewModel.canteenItems.value.isNullOrEmpty()) {
            showProgressBar()
            fetchDataFromApi()
        } else {
            hideProgressBar()
            updateRecyclerView(sharedViewModel.canteenItems.value!!)
        }

        val fullCategory = view.findViewById<TextView>(R.id.fullCategory)
        fullCategory.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, Dishes_Category())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val lunch1= view.findViewById<CardView>(R.id.cardView2)
        lunch1.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","LUNCH")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val breakfast= view.findViewById<CardView>(R.id.cardView1)
        breakfast.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","BREAKFAST")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val chinese= view.findViewById<CardView>(R.id.cardView4)
        chinese.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","CHINESE")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val dinner= view.findViewById<CardView>(R.id.cardView3)
        dinner.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","DINNER")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val italian= view.findViewById<CardView>(R.id.cardView5)
        italian.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","ITALIAN")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val chaat= view.findViewById<CardView>(R.id.cardView6)
        chaat.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","CHAAT")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val thali= view.findViewById<CardView>(R.id.cardView7)
        thali.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","THALI")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val snacks= view.findViewById<CardView>(R.id.cardView8)
        snacks.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","SNACKS")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
        val drinks= view.findViewById<CardView>(R.id.cardView9)
        drinks.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","DRINKS")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }


        /**
         * this is to implement the search adapter
         */
        searchProgressBar=view.findViewById(R.id.searchProgress)
        rvadapterSearch = RvAdapter2(ArrayList(), requireContext(), this,this,this)
        recyclerViewSearch = view.findViewById(R.id.rvSearch)
        recyclerViewSearch.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerViewSearch.adapter = rvadapterSearch
        /**
         * This is the code for the search view
         */
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        searchView.queryHint = "What would you like to eat?"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            recyclerViewSearch.visibility=View.VISIBLE
            if (!query.isNullOrEmpty()) {
                searchJob?.cancel()  // Cancel the previous job if it's still running
                searchJob = lifecycleScope.launch {
                    try {
                        showSearchProgressBar()
                        hideOtherWidgets()
                        val response = withContext(Dispatchers.IO) {
                            RetrofitInstance2.getApiServiceWithToken(dataStore).submitFormData(
                                canteenId = null,
                                foodName = query,
                                category = null,
                                lowPrice = 0.0,
                                highPrice = null,
                                veg = true,
                                rating = null
                            ).execute()
                        }
                        handleSearchResponse(response)
                    } catch (e: HttpException) {
                        // Handle HTTP exception
                    } finally {
                        hideSearchProgressBar()
                    }
                }
            }
            else{
                showOtherWidgets()
            }
            return true
        }

            private fun handleSearchResponse(response: Response<List<FoodItem>>) {
                if (response.isSuccessful) {
                    val foodItems = response.body()
                    if (!foodItems.isNullOrEmpty()) {
                        // Process and display the search results as needed
                        updateCategoryRecyclerView(foodItems)
                        Log.d("test", foodItems.toString())
                    } else {
                        // Handle case when no results are found
                        Log.d("test", "No results found.")
                    }
                } else {
                    // Handle the error
                    Log.d("test", "Error: ${response.code()}, ${response.message()}")
                }
            }



            override fun onQueryTextChange(query: String?): Boolean {
                recyclerViewSearch.visibility=View.VISIBLE
                if (!query.isNullOrEmpty()) {
                    searchJob?.cancel()  // Cancel the previous job if it's still running
                    searchJob = lifecycleScope.launch {
                        try {
                            showSearchProgressBar()
                            hideOtherWidgets()
                            delay(1000)
                            val response = withContext(Dispatchers.IO) {
                                RetrofitInstance2.getApiServiceWithToken(dataStore).submitFormData(
                                    canteenId = null,
                                    foodName = query,
                                    category = null,
                                    lowPrice = 0.0,
                                    highPrice = null,
                                    veg = true,
                                    rating = null
                                ).execute()
                            }
                            handleSearchResponse(response)
                        } catch (e: HttpException) {
                            // Handle HTTP exception
                        } finally {
                            hideSearchProgressBar()
                        }
                    }
                }else{
                    recyclerViewSearch.visibility=View.GONE
                    showOtherWidgets()
                    hideSearchProgressBar()
                }

                return true
            }
        })

        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                // Handle suggestion selection
                showToast(position.toString())
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                // Handle suggestion click
                showToast(position.toString())
                return true
            }
        })

        return view
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClickCanteen(name: String) {
        val bundle= Bundle()
        bundle.putString("key",name)
        val passing =ShowCanteenMenu()
        passing.arguments=bundle
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment, passing)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        //showToast(name)

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
        // Use lifecycleScope instead of viewModelScope
        lifecycleScope.launch {
            try {
                //showCustomProgressDialog()
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteens()
                if (response.isSuccessful) {
                    Log.d("Testing", response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                    val canteenItems = response.body()?.canteenItems.orEmpty()
                    sharedViewModel.setCanteenItems(canteenItems)
                } else {
                    // Handle the error
                    Log.d("Testing", response.body().toString())
                    Log.d(
                        "Testing",
                        "Response code: ${response.code()}, Response body: ${response.body()}"
                    )
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.d("Testing", "Network Error")
                Log.e("Testing", "Network Error: ${e.message}", e)
            } finally {
                //dismissCustomProgressDialog()
                hideProgressBar()
            }
        }
    }

    private fun updateRecyclerView(canteenItems: List<CanteenItem>) {
        val dataList = canteenItems.map { canteenItem ->
            RvModel(canteenItem.canteenImage, canteenItem.name, canteenItem.description)
        }
        rvadapter.updateData(dataList)
    }

    private fun observeCanteenItems() {
        sharedViewModel.canteenItems.observe(viewLifecycleOwner, Observer { canteenItems ->
            if (canteenItems.isNullOrEmpty()) {
                fetchDataFromApi()
            } else {
                updateRecyclerView(canteenItems)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = requireContext().createDataStore(name = "user")
        observeCanteenItems()
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
    private fun showSearchProgressBar() {
        searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideSearchProgressBar() {
        searchProgressBar.visibility = View.GONE
    }
    override fun onItemClick(name: Long) {
        showToast(name.toString())
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
//        showToast(name.toString())
//        val bundle =Bundle()
//        bundle.putString("id",name.toString())
//        val passing =ShowItem()
//        passing.arguments=bundle
//        val fragmentTransaction = parentFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.flFragment, passing)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()

    }
    private fun updateCategoryRecyclerView(canteenItems: List<FoodItem>) {
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
        rvadapterSearch.updateData(dataList)
    }
    private fun hideOtherWidgets() {
        requireView().findViewById<HorizontalScrollView>(R.id.scroll).visibility=View.GONE
        requireView().findViewById<ImageView>(R.id.imageView7)?.visibility = View.GONE
        requireView().findViewById<RecyclerView>(R.id.rvid).visibility=View.GONE
        requireView().findViewById<TextView>(R.id.canteen).visibility=View.GONE
        requireView().findViewById<TextView>(R.id.fullCategory).visibility=View.GONE
        requireView().findViewById<TextView>(R.id.category).visibility=View.GONE
        requireView().findViewById<TextView>(R.id.textView10).visibility=View.GONE
    }
    private fun showOtherWidgets() {
        requireView().findViewById<HorizontalScrollView>(R.id.scroll).visibility = View.VISIBLE
        requireView().findViewById<ImageView>(R.id.imageView7)?.visibility = View.VISIBLE
        requireView().findViewById<RecyclerView>(R.id.rvid).visibility = View.VISIBLE
        requireView().findViewById<TextView>(R.id.canteen).visibility=View.VISIBLE
        requireView().findViewById<TextView>(R.id.fullCategory).visibility=View.VISIBLE
        requireView().findViewById<TextView>(R.id.category).visibility=View.VISIBLE
        requireView().findViewById<TextView>(R.id.textView10).visibility=View.VISIBLE
    }

}

