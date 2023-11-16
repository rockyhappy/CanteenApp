package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class SearchFilter : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_search_filter, container, false)

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
                veg.setTextColor(getResources().getColor(R.color.grey))
                veg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
                nonveg.setTextColor(getResources().getColor(R.color.grey))
                nonveg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                vegChecked=true
                vegUnchecked=false
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
                nonveg.setTextColor(getResources().getColor(R.color.grey))
                nonveg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
                veg.setTextColor(getResources().getColor(R.color.grey))
                veg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }
            else{
                nonvegChecked=true
                nonvegUnchecked=false
                nonveg.setTextColor(getResources().getColor(R.color.primary_color))
                nonveg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border2)
                veg.setTextColor(getResources().getColor(R.color.grey))
                veg.background= ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border)
            }

        }

        return view
    }

}