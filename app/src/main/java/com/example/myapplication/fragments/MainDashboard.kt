package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

class MainDashboard : Fragment(R.layout.fragment_main_dashboard) , RvAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
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
        lifecycleScope.launch {
            try {
                showCustomProgressDialog()
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteens()
                if (response.isSuccessful) {
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                    val canteenItems = response.body()?.canteenItems.orEmpty()

                    // Convert CanteenItem objects to RvModel objects
                    val dataList = canteenItems.map { canteenItem ->
                        RvModel(canteenItem.canteenImage, canteenItem.name, canteenItem.description)
                    }
                    rvadapter.updateData(dataList)
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

}

