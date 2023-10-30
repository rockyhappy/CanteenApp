package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ApiService
import com.example.myapplication.LoginRequest
import com.example.myapplication.R
import com.example.myapplication.SignUpRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern


class Registrationfragment : Fragment(R.layout.fragment_registrationfragment) {

    private lateinit var dataStore: DataStore<Preferences>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_registrationfragment, container, false)

        /** this is the Back Button */
        val backButton:FloatingActionButton=view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ChoiceFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        /**This is the Sign In text*/
        val signIn =view.findViewById<TextView>(R.id.textView2)
        signIn.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, LoginFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        /** This is the main Button of the api calling*/
        val button=view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var collection:TextInputEditText=view.findViewById(R.id.email)
            var UserName=collection.text.toString()
            UserName=UserName.trim()

            collection=view.findViewById(R.id.email2)
            var Email=collection.text.toString()
            Email=Email.trim()

            collection=view.findViewById(R.id.passkey)
            var password1=collection.text.toString()
            password1.trim()

            collection=view.findViewById(R.id.passkey2)
            var password2=collection.text.toString()
            password2.trim()


            //Flag if all is correct
            var flag=false

            /** Declairing the variables for the */
            var userNameIncorrect =view.findViewById<TextView>(R.id.userNameIncorrect)
            var userMailIncorrect=view.findViewById<TextView>(R.id.userMailIncorrect)
            var userPassword1=view.findViewById<TextView>(R.id.passwordIncorrect1)
            var userPassword2=view.findViewById<TextView>(R.id.passwordIncorrect2)

            /** Checking for user name correctness*/
            if(UserName.length<6)
            {
                userNameIncorrect.text="User Name should be more than  6"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout)
                text1.setBackgroundResource(R.drawable.button_layout)

            }
            else {
                userNameIncorrect.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout)
                text1.setBackgroundResource(R.drawable.inputbox)

            }
            /**Checking For Email Correctness*/
            if(!isValidEmail(Email))
            {
                userMailIncorrect.text="Not a Valid Mail"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout2)
                text1.setBackgroundResource(R.drawable.button_layout)

            }
            else {
                userMailIncorrect.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout2)
                text1.setBackgroundResource(R.drawable.inputbox)

            }

            /**Checking for if password is valid*/
            if(!isValidPassword(password1))
            {
                userPassword1.text="Password not strong"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout3)
                text1.setBackgroundResource(R.drawable.button_layout)

            }
            else{
                userPassword1.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout3)
                text1.setBackgroundResource(R.drawable.inputbox)

            }
            if(password2!=password1)
            {
                userPassword2.text="Confirm Password Should match Password"
                flag=true
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout4)
                text1.setBackgroundResource(R.drawable.button_layout)

            }
            else {
                userPassword2.text=""
                val text1: TextInputLayout = view.findViewById(R.id.textInputLayout4)
                text1.setBackgroundResource(R.drawable.inputbox)

            }
            /** API testing */
            if(false)
            {
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.flFragment, RegistrationVerifyMail())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            /**
             * this is the code for the original api
             */
            if(!flag) {
                val signUpRequest = SignUpRequest(
                    fullName=UserName,
                    email=Email,
                    password=password1,
                    role="USER"
                )
                lifecycleScope.launch {
                    val response = RetrofitInstance.apiService.fetchData(signUpRequest)
                    Log.d("error",response.body().toString())
                    if (response.isSuccessful) {
                        if(response.body()?.token.toString()=="Check your email for OTP"){
                            dataStore= context?.createDataStore(name= "user")!!
                            save("Email",Email)
                            save("password",password1)
                            save("fullname",UserName)
                            val fragmentTransaction = parentFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.flFragment, RegistrationVerifyMail())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        else{
                            showToast("User already Exists")
                        }
                    }
                    Log.d("API Error", "Response code: ${response.code()}, Message: ${response.message()}")
                }
            }
            else
            {
                showToast("Something went Wrong Please Retry")
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
    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }


    private suspend fun read (key:String) : String?{
        val dataStoreKey= preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]

    }
    private suspend fun save (key:String , value:String){
        val dataStoreKey= preferencesKey<String>(key)
        dataStore.edit{temp ->
            temp[dataStoreKey]=value
        }
    }
}
object RetrofitInstance {
    private const val BASE_URL = "https://brunchbliss.onrender.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}