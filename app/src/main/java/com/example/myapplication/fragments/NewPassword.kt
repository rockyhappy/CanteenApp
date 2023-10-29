package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NewPassword : Fragment(R.layout.fragment_new_password) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_new_password, container, false)

        //main code here

        val svgImageView2 = view.findViewById<SVGImageView>(R.id.svgImageView2)
        var svg2 = SVG.getFromResource(resources, R.raw.new_password)
        svgImageView2.setSVG(svg2)

        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            /**
             * this is the position to call the api
             */

            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, WellDone())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, VerifyMail())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        return view
    }


}