package com.example.myapplication.fragments

import android.media.Image
import android.os.Bundle
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
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import kotlinx.coroutines.launch


class ShowItem : Fragment() {
    private lateinit var dataStore: DataStore<Preferences>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_show_item, container, false)
        dataStore = requireContext().createDataStore(name = "user")
        val image=view.findViewById<ImageView>(R.id.ImageView)
        val textView1=view.findViewById<TextView>(R.id.textView1)
        val textView2=view.findViewById<TextView>(R.id.textView2)
        val textView3=view.findViewById<TextView>(R.id.textView3)
        val textView4=view.findViewById<TextView>(R.id.textView4)
        val textView5=view.findViewById<TextView>(R.id.textView5)
        val textView6=view.findViewById<TextView>(R.id.textView6)
        val textView7=view.findViewById<TextView>(R.id.textView7)
        val textView8=view.findViewById<TextView>(R.id.textView8)
        val receivedData = arguments?.getString("id")

        lifecycleScope.launch{
            try{
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getFoodDetail(receivedData.toString())
                if(response.isSuccessful)
                {
                    textView1.text=response.body()?.id.toString()
                    textView2.text=response.body()?.name
                    textView3.text=response.body()?.category
                    textView4.text=response.body()?.price.toString()
                    textView5.text=response.body()?.canteenId.toString()
                    textView6.text=response.body()?.foodImage
                    textView7.text=response.body()?.description
                }
                else {
                    showToast("Error")
                }
            }catch(e: Exception){
                showToast("Error")
            }finally {
                showToast("workDone")
            }
        }


        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}