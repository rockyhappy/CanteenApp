package com.example.myapplication.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ApiService
import com.example.myapplication.R
import com.example.myapplication.RvModel
import com.example.myapplication.readFromDataStore
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter


class Breakfast : Fragment(), RvAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataStore = requireContext().createDataStore(name = "user")


        val view = inflater.inflate(R.layout.fragment_breakfast, container, false)
        rvadapter = RvAdapter(ArrayList(), requireContext(), this)
        recyclerView = view.findViewById(R.id.rvid)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
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

        return view
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClickCanteen(name: String) {
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






