package com.example.myapplication.fragments

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R


class ShowItem : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_show_item, container, false)

        val image=view.findViewById<ImageView>(R.id.ImageView)
        val textView1=view.findViewById<TextView>(R.id.textView1)
        val textView2=view.findViewById<TextView>(R.id.textView2)
        val textView3=view.findViewById<TextView>(R.id.textView3)
        val textView4=view.findViewById<TextView>(R.id.textView4)
        val textView5=view.findViewById<TextView>(R.id.textView5)



        return view
    }


}