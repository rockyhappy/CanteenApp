package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.example.myapplication.R
import com.example.myapplication.forgotPasswordRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class ForgotPassward : Fragment(R.layout.fragment_forgot_passward) {

    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_passward, container, false)


        val svgImageView2 = view.findViewById<SVGImageView>(R.id.svgImageView2)
        var svg2 = SVG.getFromResource(resources, R.raw.forgot_password_image)
        svgImageView2.setSVG(svg2)


        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val container = view.findViewById<ConstraintLayout>(R.id.Container)
        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            /**call api for the forgot password*/
            var collection : TextInputEditText =view.findViewById(R.id.email)
            button.isEnabled = false
            container.isEnabled=false
            container.isFocusable = false
//            progressBar.visibility = View.VISIBLE
            showCustomProgressDialog()
            var Email=collection.text.toString()
            Email=Email.trim()

            var flag=false

            var userMailIncorrect=view.findViewById<TextView>(R.id.userMailIncorrect)

            if(!isValidEmail(Email))
            {
                userMailIncorrect.text="Not a Valid Mail"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout)
                text1.setBackgroundResource(R.drawable.button_layout)

            }
            else {
                userMailIncorrect.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout)
                text1.setBackgroundResource(R.drawable.inputbox)

            }
            if(flag)
            {
                // Enable the button
                button.isEnabled = true
                container.isEnabled=true
                container.isFocusable = true
//                progressBar.visibility = View.GONE
                dismissCustomProgressDialog()
            }

            if(!flag)
            {
                val forgotPasswordRequest=forgotPasswordRequest(
                    email=Email
                )
                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.apiService.ForgotPassward(forgotPasswordRequest)
                        Log.d("error", response.body().toString())
                        if (response.isSuccessful) {
                                dataStore = context?.createDataStore(name = "user")!!
                                save("Email", Email)
                                val fragmentTransaction = parentFragmentManager.beginTransaction()
                                fragmentTransaction.replace(R.id.flFragment, VerifyMail())
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                        }
                        else {
                            userMailIncorrect.text ="Invalid Credentials"
                            val text1: TextInputLayout = view.findViewById(R.id.textInputLayout)
                            text1.setBackgroundResource(R.drawable.button_layout)
                        }

                    }catch(e : Exception)
                    {
                        showToast("Connection Error")
                    }
                    finally {
                        button.isEnabled = true
                        container.isEnabled=true
                        container.isFocusable = true
//                        progressBar.visibility = View.GONE
                        dismissCustomProgressDialog()
                    }
                    //Log.d("API Error", "Response code: ${response.code()}, Message: ${response.message()}")
                }
            }
//            val fragmentTransaction = parentFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.flFragment, VerifyMail())
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
        }

        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, LoginFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
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
    private suspend fun save (key:String , value:String){
        val dataStoreKey= preferencesKey<String>(key)
        dataStore.edit{temp ->
            temp[dataStoreKey]=value
        }
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