package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.google.android.material.card.MaterialCardView


class Dishes_Category : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_dishes__category, container, false)

        val lunch1= view.findViewById<CardView>(R.id.cardView1)

        //val newOutlineColor = ContextCompat.getColor(requireContext(), R.drawable.round_corner)
        //lunch1.setBackgroundResource(R.drawable.primary_color_layout)
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
        val breakfast= view.findViewById<CardView>(R.id.cardView2)
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
        val chinese= view.findViewById<CardView>(R.id.cardView3)
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
        val dinner= view.findViewById<CardView>(R.id.cardView4)
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



        return view
    }

}