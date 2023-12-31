package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.R
import com.example.myapplication.ResetPasswordRequest
import com.example.myapplication.readFromDataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern

class NewPassword : Fragment(R.layout.fragment_new_password) {

    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = requireContext().createDataStore(name = "user")
    }
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


        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val container = view.findViewById<ConstraintLayout>(R.id.Container)
        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            /**
             * this is the position to call the api
             */

            button.isEnabled = false
            container.isEnabled=false
            container.isFocusable = false
            showCustomProgressDialog()


            var collection : TextInputEditText =view.findViewById(R.id.passkey)
            var password1=collection.text.toString()
            password1.trim()

            collection=view.findViewById(R.id.passkey2)
            var password2=collection.text.toString()
            password2.trim()

            var flag=false

            var userPassword1=view.findViewById<TextView>(R.id.passwordIncorrect1)
            var userPassword2=view.findViewById<TextView>(R.id.passwordIncorrect2)
            if(!isValidPassword(password1))
            {
                userPassword1.text="Password not strong"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout2)
                text1.setBackgroundResource(R.drawable.button_layout)

            }
            else{
                userPassword1.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout2)
                text1.setBackgroundResource(R.drawable.inputbox)

            }
            if(password2!=password1)
            {
                userPassword2.text="Confirm Password Should match Password"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout1)
                text1.setBackgroundResource(R.drawable.button_layout)
            }
            else {
                userPassword2.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout1)
                text1.setBackgroundResource(R.drawable.inputbox)

            }

            /** API testing */
            if(flag)
            {
                // Enable the button
                button.isEnabled = true
                container.isEnabled=true
                container.isFocusable = true
                dismissCustomProgressDialog()
            }

            if(!flag)
            {
                lifecycleScope.launch{
                    try{
                        val Email = readFromDataStore(dataStore, "Email")
                        val resetPasswordRequest=ResetPasswordRequest(
                            email=Email.toString(),
                            newPassword = password1
                        )
                        val response=RetrofitInstance.apiService.ResetPassword(resetPasswordRequest)
                        Log.d("error", response.body().toString())
                        if (response.isSuccessful) {
                                val fragmentTransaction = parentFragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.flFragment, WellDone())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                        }
                    }catch (e:Exception)
                    {
                        showToast("Connection Error")
                    }
                    finally {
                        button.isEnabled = true
                        container.isEnabled=true
                        container.isFocusable = true
                        dismissCustomProgressDialog()
                    }
                }

            }
            else {
                button.isEnabled = true
                container.isEnabled=true
                container.isFocusable = true
                dismissCustomProgressDialog()
            }
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
    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun showCustomProgressDialog() {
        dialog = Dialog(requireContext())
        dialog?.setContentView(R.layout.custom_dialog_loading)
        dialog?.setCancelable(false)

        val window = dialog?.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog?.show()
    }
    private fun dismissCustomProgressDialog() {
        dialog?.dismiss()
        dialog = null
    }

}