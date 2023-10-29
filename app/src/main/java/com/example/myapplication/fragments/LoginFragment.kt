package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.DashBoard
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val forgotPassword = view.findViewById<TextView>(R.id.forgot_pass)

        forgotPassword.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ForgotPassward())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            /** call api and apply checks*/
            startActivity(Intent(requireActivity(), DashBoard::class.java))
            requireActivity().finish()
        }

        /** This is the BackButton */
        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ChoiceFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val signUp =view.findViewById<TextView>(R.id.textView2)
        signUp.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, Registrationfragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val textView2 =view.findViewById<TextView>(R.id.signUp)
        textView2.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, Registrationfragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        return view
    }
}