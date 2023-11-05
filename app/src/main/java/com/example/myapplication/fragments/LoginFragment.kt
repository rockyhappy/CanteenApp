package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
import com.example.myapplication.DashBoard
import com.example.myapplication.LoginRequest
import com.example.myapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var dataStore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = requireContext().createDataStore(name = "user")
    }
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

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val container = view.findViewById<ConstraintLayout>(R.id.Container)
        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            /** call api and apply checks*/

            var flag=false
            button.isEnabled = false
            container.isEnabled=false
            container.isFocusable = false
            progressBar.visibility = View.VISIBLE

            var collection : TextInputEditText =view.findViewById(R.id.email)
            var Email=collection.text.toString()
            Email=Email.trim()

            var userMailIncorrect =view.findViewById<TextView>(R.id.userMailIncorrect)
            if(!isValidEmail(Email))
            {
                userMailIncorrect.text="Not a Valid Mail"
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout)
                text1.setBackgroundResource(R.drawable.button_layout)
                flag=true
            }
            else {
                userMailIncorrect.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout)
                text1.setBackgroundResource(R.drawable.inputbox)

            }


            collection=view.findViewById(R.id.passkey)
            var password1=collection.text.toString()
            password1.trim()

            var userPassword1=view.findViewById<TextView>(R.id.passwordIncorrect1)
            if(!isValidPassword(password1))
            {
                userPassword1.text="Password not Applicable"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout2)
                text1.setBackgroundResource(R.drawable.button_layout)
            }
            else{
                userPassword1.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout2)
                text1.setBackgroundResource(R.drawable.inputbox)
            }

            /** API testing */
            if(flag)
            {
                // Enable the button
                button.isEnabled = true
                container.isEnabled=true
                container.isFocusable = true
                progressBar.visibility = View.GONE

                /**
                 * This is bypassing the checkpoint
                 */
//                startActivity(Intent(requireActivity(), DashBoard::class.java))
//                requireActivity().finish()
            }
            if(!flag)
            {
                val loginRequest=LoginRequest(
                email=Email,
                password = password1)
                lifecycleScope.launch{

                    try {


                        val response = RetrofitInstance.apiService.login(loginRequest)
                        if (response.isSuccessful) {
                            //if(response.body()?.token.toString()!="OTP Expired" || response.body()?.token.toString()!="Incorrect OTP"||response.body()?.token.toString()!="No OTP generated"){
                            if (response.body()?.token.toString().length >= 30) {
                                save("token", response.body()?.token.toString())
//                            val fragmentTransaction = parentFragmentManager.beginTransaction()
//                            fragmentTransaction.replace(R.id.flFragment, Congratulationsfragment())
//                            fragmentTransaction.addToBackStack(null)
//                            fragmentTransaction.commit()
                                startActivity(Intent(requireActivity(), DashBoard::class.java))
                                requireActivity().finish()
                            } else {
                                val incorrectOtp =
                                    view.findViewById<TextView>(R.id.passwordIncorrect1)
                                incorrectOtp.text = response.body()?.token.toString()
                            }
                        } else {
                            showToast("Please Retry")
                        }
                    }catch(e : Exception){
                        showToast("Connection Error")
                    }finally {
                        button.isEnabled = true
                        container.isEnabled=true
                        container.isFocusable = true
                        progressBar.visibility = View.GONE
                    }
                }

            }

//            startActivity(Intent(requireActivity(), DashBoard::class.java))
//            requireActivity().finish()
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
            parentFragmentManager.popBackStack()
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
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
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
    private suspend fun save (key:String , value:String){
        val dataStoreKey= preferencesKey<String>(key)
        dataStore.edit{temp ->
            temp[dataStoreKey]=value
        }
    }
}