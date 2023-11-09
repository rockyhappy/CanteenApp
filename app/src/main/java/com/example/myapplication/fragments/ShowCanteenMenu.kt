package com.example.myapplication.fragments

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
import com.example.myapplication.GetFoodByCanteenRequest
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter
import com.example.myapplication.RvModel
import kotlinx.coroutines.launch

class ShowCanteenMenu : Fragment() , RvAdapter.OnItemClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataStore = requireContext().createDataStore(name = "user")
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_show_canteen_menu, container, false)
        rvadapter = RvAdapter(ArrayList(), requireContext(), this)
        recyclerView = view.findViewById<RecyclerView>(R.id.rvi)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = rvadapter

        val receivedData = arguments?.getString("key").toString()
        showToast(receivedData)
        lifecycleScope.launch {
            try {

                showCustomProgressDialog()
                val request=GetFoodByCanteenRequest(
                    name =receivedData.toString()
                    //name ="Sarthak ki dukaan"
                )
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteenFood(request)
                if (response.isSuccessful) {
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                    val canteenItems = response.body()?.foodItems.orEmpty()

                    // Convert CanteenItem objects to RvModel objects
                    val dataList = canteenItems.map { canteenItem ->
                        RvModel(canteenItem.category, canteenItem.name, canteenItem.description)
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



        return view;
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClick(name: String) {
        showToast(name)

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