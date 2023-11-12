package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter2
import kotlinx.coroutines.launch

class cart : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter2
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_cart, container, false)
        dataStore = requireContext().createDataStore(name = "user")
        lifecycleScope.launch{
            try{
//                val response = RetrofitInstance2.getApiServiceWithToken(dataStore = )
            }catch (e: Exception)
            {

            }finally{

            }
        }
        return view;
    }


}