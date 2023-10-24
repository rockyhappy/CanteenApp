package com.example.myapplication.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.R
import com.google.android.material.textfield.TextInputEditText



class Registrationfragment : Fragment(R.layout.fragment_registrationfragment) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_registrationfragment, container, false)

        val svgImageView = view.findViewById<SVGImageView>(R.id.svgImageView)
        var svg = SVG.getFromResource(resources, R.raw.backbutton)
        svgImageView.setSVG(svg)
        svgImageView.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ChoiceFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val button=view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var userName:TextInputEditText=view.findViewById(R.id.email)
            val UserName=userName.text.toString()

            userName=view.findViewById(R.id.email2)
            val Email=userName.text.toString()

            userName=view.findViewById(R.id.passkey)
            val password1=userName.text.toString()

            userName=view.findViewById(R.id.passkey2)
            val password2=userName.text.toString()

            if(isValidEmail(Email)&&password1==password2)
            {
                showToast(password1)
            }
            else
            {
                showToast("Kuch toh gadbad hai daya")
            }

        }
        return view
    }
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}