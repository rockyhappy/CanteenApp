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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.CanteenItem
import com.example.myapplication.ViewModel.SharedViewModel
import androidx.lifecycle.viewModelScope


class MainDashboard : Fragment(R.layout.fragment_main_dashboard) , RvAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    private  val sharedViewModel: SharedViewModel  by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataStore = requireContext().createDataStore(name = "user")
        val view= inflater.inflate(R.layout.fragment_main_dashboard, container, false)
        rvadapter = RvAdapter(ArrayList(), requireContext(), this)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvid)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = rvadapter
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(recyclerView)

        if (sharedViewModel.canteenItems.value.isNullOrEmpty()) {
            fetchDataFromApi()
        } else {
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
         * This is the code for the search view
         */
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        searchView.queryHint = "What would you like to eat?"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle the submission of the query
                showToast(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle changes to the query text
                showToast(newText.toString())
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







        return view;
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClick(name: String) {
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
                showCustomProgressDialog()
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
                dismissCustomProgressDialog()
            }
        }
    }

    private fun updateRecyclerView(canteenItems: List<CanteenItem>) {
        val dataList = canteenItems.map { canteenItem ->
            RvModel(canteenItem.canteenImage, canteenItem.name, canteenItem.description)
        }
        rvadapter.updateData(dataList)
    }


    // Assuming you have a function to observe the data in your SharedViewModel
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



}

