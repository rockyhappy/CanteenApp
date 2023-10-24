package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.R


class VerifyMail : Fragment(R.layout.fragment_verify_mail) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verify_mail, container, false)

        val svgImageView = view.findViewById<SVGImageView>(R.id.svgImageView)
        var svg = SVG.getFromResource(resources, R.raw.backbutton)
        svgImageView.setSVG(svg)
        val svgImageView2 = view.findViewById<SVGImageView>(R.id.svgImageView2)
        var svg2 = SVG.getFromResource(resources, R.raw.otp_verification)
        svgImageView2.setSVG(svg2)


        svgImageView.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ForgotPassward())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, VerifyMail())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        var editText1=view.findViewById<EditText>(R.id.editText1)
        var editText2=view.findViewById<EditText>(R.id.editText2)
        var editText3=view.findViewById<EditText>(R.id.editText3)
        var editText4=view.findViewById<EditText>(R.id.editText4)
        //GenericTextWatcher here works only for moving to next EditText when a number is entered
        //first parameter is the current EditText and second parameter is next EditText
        editText1.addTextChangedListener(GenericTextWatcher(editText1, editText2))
        editText2.addTextChangedListener(GenericTextWatcher(editText2, editText3))
        editText3.addTextChangedListener(GenericTextWatcher(editText3, editText4))
        editText4.addTextChangedListener(GenericTextWatcher(editText4, null))

        //GenericKeyEvent here works for deleting the element and to switch back to previous EditText
        //first parameter is the current EditText and second parameter is previous EditText
        editText1.setOnKeyListener(GenericKeyEvent(editText1, null))
        editText2.setOnKeyListener(GenericKeyEvent(editText2, editText1))
        editText3.setOnKeyListener(GenericKeyEvent(editText3, editText2))
        editText4.setOnKeyListener(GenericKeyEvent(editText4,editText3))

        return view
    }
}

class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.editText1 && currentView.text.isEmpty()) {
            //If current is empty then previous EditText's number will also be deleted
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }


}

class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
        val text = editable.toString()
        when (currentView.id) {
            R.id.editText1 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.editText2 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.editText3 -> if (text.length == 1) nextView!!.requestFocus()
//            R.id.editText4 -> if (text.length == 1) start(currentView)
            //You can use EditText4 same as above to hide the keyboard
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

}


//fun start(view :View)
//{
//    val fragmentTransaction = parentFragmentManager.beginTransaction()
//    fragmentTransaction.replace(R.id.flFragment, VerifyMail())
//    fragmentTransaction.addToBackStack(null)
//    fragmentTransaction.commit()
//}