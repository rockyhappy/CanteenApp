package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.R

class ForgotPassward : Fragment(R.layout.fragment_forgot_passward) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_passward, container, false)

        val svgImageView = view.findViewById<SVGImageView>(R.id.svgImageView)
        var svg = SVG.getFromResource(resources, R.raw.backbutton)
        svgImageView.setSVG(svg)
        val svgImageView2 = view.findViewById<SVGImageView>(R.id.svgImageView2)
        var svg2 = SVG.getFromResource(resources, R.raw.forgot_password_image)
        svgImageView2.setSVG(svg2)


        return view
    }
}