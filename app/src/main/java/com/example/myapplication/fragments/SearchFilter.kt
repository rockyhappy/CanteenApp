package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter
import com.example.myapplication.RvAdapterSearch
import com.example.myapplication.RvModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class SearchFilter : Fragment(R.layout.fragment_search_filter) , RvAdapterSearch.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapterSearch
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_search_filter, container, false)
        dataStore = requireContext().createDataStore(name = "user")
        rvadapter = RvAdapterSearch(ArrayList(), requireContext(), this)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvi)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),3, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = rvadapter

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

        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }





        val veg = view.findViewById<Button>(R.id.veg)
        val nonveg=view.findViewById<Button>(R.id.nonveg)
        var vegChecked=false
        var vegUnchecked=true
        var nonvegChecked=false
        var nonvegUnchecked=true
        veg.setOnClickListener {
            if(vegChecked)
            {
                vegChecked=false
                vegUnchecked=true
                nonvegChecked=false
                nonvegUnchecked=true
                veg.setTextColor(getResources().getColor(R.color.grey))
                veg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
                nonveg.setTextColor(getResources().getColor(R.color.grey))
                nonveg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                vegChecked=true
                vegUnchecked=false
                nonvegChecked=false
                nonvegUnchecked=true
                veg.setTextColor(getResources().getColor(R.color.primary_color))
                veg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
                nonveg.setTextColor(getResources().getColor(R.color.grey))
                nonveg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }

        }

        nonveg.setOnClickListener {

            if(nonvegChecked)
            {
                nonvegChecked=false
                nonvegUnchecked=true
                vegChecked=false
                vegUnchecked=true
                nonveg.setTextColor(getResources().getColor(R.color.grey))
                nonveg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
                veg.setTextColor(getResources().getColor(R.color.grey))
                veg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                nonvegChecked=true
                nonvegUnchecked=false
                vegChecked=false
                vegUnchecked=true
                nonveg.setTextColor(getResources().getColor(R.color.primary_color))
                nonveg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
                veg.setTextColor(getResources().getColor(R.color.grey))
                veg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }

        }
        val breakfast=view.findViewById<Button>(R.id.breakfast)
        var breakfastClick=false
        var breakfastUnclick=true
        val lunch=view.findViewById<Button>(R.id.lunch)
        var lunchClick=false
        var lunchUnclick= true
        val dinner=view.findViewById<Button>(R.id.dinner)
        var dinnerClick=false
        var dinnerUnclick=true

        breakfast.setOnClickListener {
            if(breakfastClick)
            {
                breakfastClick=false
                breakfastUnclick=true
                breakfast.setTextColor(getResources().getColor(R.color.grey))
                breakfast.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else
            {
                breakfastClick=true
                breakfastUnclick=false
                breakfast.setTextColor(getResources().getColor(R.color.primary_color))
                breakfast.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
            }
        }

        lunch.setOnClickListener {
            if(lunchClick)
            {
                lunchClick=false
                lunchUnclick=true
                lunch.setTextColor(getResources().getColor(R.color.grey))
                lunch.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else
            {
                lunchClick=true
                lunchUnclick=false
                lunch.setTextColor(getResources().getColor(R.color.primary_color))
                lunch.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
            }
        }

        dinner.setOnClickListener {
            if(dinnerClick)
            {
                dinnerClick=false
                dinnerUnclick=true
                dinner.setTextColor(getResources().getColor(R.color.grey))
                dinner.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else
            {
                dinnerClick=true
                dinnerUnclick=false
                dinner.setTextColor(getResources().getColor(R.color.primary_color))
                dinner.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
            }
        }
        val star1=view.findViewById<Button>(R.id.star1)
        var star1Clicked=false
        star1.setOnClickListener {
            if(star1Clicked){
                star1Clicked=false
                star1.setTextColor(getResources().getColor(R.color.grey))
                star1.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                star1Clicked=true
                star1.setTextColor(getResources().getColor(R.color.primary_color))
                star1.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
            }
        }
        val star2=view.findViewById<Button>(R.id.star2)
        var star2Clicked=false
        star2.setOnClickListener {
            if(star2Clicked){
                star2Clicked=false
                star2.setTextColor(getResources().getColor(R.color.grey))
                star2.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                star2Clicked=true
                star2.setTextColor(getResources().getColor(R.color.primary_color))
                star2.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
            }
        }
        val star3=view.findViewById<Button>(R.id.star3)
        var star3Clicked=false
        star3.setOnClickListener {
            if(star3Clicked){
                star3Clicked=false
                star3.setTextColor(getResources().getColor(R.color.grey))
                star3.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                star3Clicked=true
                star3.setTextColor(getResources().getColor(R.color.primary_color))
                star3.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
            }
        }
        val star4=view.findViewById<Button>(R.id.star4)
        var star4Clicked=false
        star4.setOnClickListener {
            if(star4Clicked){
                star4Clicked=false
                star4.setTextColor(getResources().getColor(R.color.grey))
                star4.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                star4Clicked=true
                star4.setTextColor(getResources().getColor(R.color.primary_color))
                star4.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
            }
        }

        return view
    }
    override fun onItemClick(name: String) {
//        val bundle= Bundle()
//        bundle.putString("key",name)
//        val passing =ShowCanteenMenu()
//        passing.arguments=bundle
//        val fragmentTransaction = parentFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.flFragment, passing)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
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