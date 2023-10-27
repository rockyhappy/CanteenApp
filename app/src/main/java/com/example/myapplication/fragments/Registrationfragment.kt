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

        val backButton:FloatingActionButton=view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ChoiceFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val button=view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            var collection:TextInputEditText=view.findViewById(R.id.email)
            val UserName=collection.text.toString()

            collection=view.findViewById(R.id.email2)
            val Email=collection.text.toString()

            collection=view.findViewById(R.id.passkey)
            val password1=collection.text.toString()

            collection=view.findViewById(R.id.passkey2)
            val password2=collection.text.toString()


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
            }
            else {
                userNameIncorrect.text=""
            }
            /**Checking For Email Correctness*/
            if(!isValidEmail(Email))
            {
                userMailIncorrect.text="Not a Valid Mail"
                flag=true
            }
            else {
                userMailIncorrect.text=""
            }

            /**Checking for if password is valid*/
            if(!isValidPassword(password1))
            {
                userPassword1.text="Password not strong"
                flag=true
            }
            else{
                userPassword1.text=""
            }
            if(password2!=password1)
            {
                userPassword2.text="Confirm Password Should match Password"
                flag=true
            }
            else {
                userPassword2.text=""
            }
            if(!flag) {
                val signUpRequest = SignUpRequest(
                     username= UserName,
                 name= "Rachit",
                 email= Email,
                 password= password1
                )
                lifecycleScope.launch {
                    val response = RetrofitInstance.apiService.fetchData(signUpRequest)
                    Log.d("error",response.body().toString())
                    if (response.isSuccessful) {
                        if(response.body()?.success.toString()=="true"){
                            dataStore= context?.createDataStore(name= "user")!!
                            save(
                                "Email",Email
                            )
                            val fragmentTransaction = parentFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.flFragment, RegistrationVerifyMail())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        }
                        else{
                            showToast("User already Exists")
                        }
                    }

                }
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
        dataStore.edit{Email ->
            Email[dataStoreKey]=value
        }
    }
}
object RetrofitInstance {
    private const val BASE_URL = "https://udemy-nx1v.onrender.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}