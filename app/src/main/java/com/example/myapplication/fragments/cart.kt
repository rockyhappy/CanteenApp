package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.RvAdapter2
import com.example.myapplication.RvAdapterCart
import kotlinx.coroutines.launch

class cart : Fragment() ,RvAdapterCart.OnDeleteClickListener,RvAdapterCart.OnItemClickListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapterCart
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
    override fun onItemClick(name: Long) {
//        val bundle =Bundle()
//        bundle.putString("id",name.toString())
//        val passing =ShowItem()
//        passing.arguments=bundle
//        val fragmentTransaction = parentFragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.flFragment, passing)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
        showToast(name.toString())


    }

    override fun onDeleteClick(name: Long) {
        TODO("Not yet implemented")
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}