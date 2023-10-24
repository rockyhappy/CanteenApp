package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.R
import com.example.myapplication.Registration

class ChoiceFragment : Fragment(R.layout.fragment_choice) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view=inflater.inflate(R.layout.fragment_choice, container, false)

        val svgImageView2 = view.findViewById<SVGImageView>(R.id.svgImageView2)
        var svg2 = SVG.getFromResource(resources, R.raw.couple_eats)
        svgImageView2.setSVG(svg2)

        val button=view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, LoginFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val button2=view.findViewById<Button>(R.id.button2)
        button2.setOnClickListener {
            startActivity(Intent(requireActivity(), Registration::class.java))
        }
        return view
    }

}