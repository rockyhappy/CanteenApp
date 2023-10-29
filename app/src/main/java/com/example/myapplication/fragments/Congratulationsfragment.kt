package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.myapplication.DashBoard
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Congratulationsfragment : Fragment(R.layout.fragment_congratulationsfragment) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_congratulationsfragment, container, false)

        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ChoiceFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val button=view.findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            startActivity(Intent(requireActivity(), DashBoard::class.java))
            requireActivity().finish()
        }


        return view
    }

}