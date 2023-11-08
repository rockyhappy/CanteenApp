package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.myapplication.R


class Dishes_Category : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_dishes__category, container, false)

        val lunch1= view.findViewById<CardView>(R.id.cardView1)
        lunch1.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("key2","lunch")
            val passing=ShowCategoryMenu()
            passing.arguments=bundle
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, passing)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }


        return view
    }

}