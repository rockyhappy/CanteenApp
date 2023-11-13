package com.example.myapplication.fragments

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.ApiService
import com.example.myapplication.LoginRequest
import com.example.myapplication.R
import com.example.myapplication.SignUpRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.regex.Matcher
import java.util.regex.Pattern




class Registrationfragment : Fragment(R.layout.fragment_registrationfragment) {

    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null

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
            parentFragmentManager.popBackStack()
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, LoginFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        //val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val container = view.findViewById<ConstraintLayout>(R.id.Container)
        /** This is the main Button of the api calling*/
        val button=view.findViewById<Button>(R.id.button)
        button.setOnClickListener {

//            //setting the progressbar
//            val mProgressDialog = ProgressDialog(requireContext())
//            mProgressDialog.setTitle("This is TITLE")
//            mProgressDialog.setMessage("This is MESSAGE")
//            mProgressDialog.show()



            // Disable the button
//            button.isEnabled = false
//            container.isEnabled=false
//            container.isFocusable = false
            //progressBar.visibility = View.VISIBLE

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
//            if(flag)
//            {
//                // Enable the button
//                button.isEnabled = true
//                container.isEnabled=true
//                container.isFocusable = true
//                progressBar.visibility = View.GONE
//            }
            /**
             * this is the code for the original api
             */
            if(!flag) {

                showCustomProgressDialog()

                val signUpRequest = SignUpRequest(
                    fullName=UserName,
                    email=Email,
                    password=password1,
                    role="USER"
                )
                lifecycleScope.launch {
                    try {

                        val response = RetrofitInstance.apiService.fetchData(signUpRequest)
                        Log.d("error", response.body().toString())
                        if (response.isSuccessful) {
                            if (response.body()?.token.toString() == "null" && response.code().toString()=="200") {
                                dataStore = context?.createDataStore(name = "user")!!
                                save("Email", Email)
                                save("fullname", UserName)

                                val fragmentTransaction = parentFragmentManager.beginTransaction()
                                fragmentTransaction.replace(
                                    R.id.flFragment,
                                    RegistrationVerifyMail()
                                )
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            } else {
                                showToast("User already Exists")
                            }
                        }else showToast("User Already Exists with the same Email")
                    }catch(e : Exception){
                        showToast("Connection Error")
                    }finally {
//                        progressDialog?.dismiss()
//                        progressDialog = null
                        dismissCustomProgressDialog()
                        //showCustomProgressDialog()
                        button.isEnabled = true
                        container.isEnabled=true
                        container.isFocusable = true
                        //progressBar.visibility = View.GONE
                    }
                    //Log.d("API Error", "Response code: ${response.code()}, Message: ${response.message()}")
                }
            }
            else
            {
                //showToast("Something went Wrong Please Retry")
//                button.isEnabled = true
//                container.isEnabled=true
//                container.isFocusable = true
                dismissCustomProgressDialog()
                //progressBar.visibility = View.GONE
            }

        }
        return view
    }

    /**
     * This is the regular expression for the email
     */
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    /**
     * this is the function to show the toast
     */
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * This is the regular expression for the password
     */
    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=]).{8,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }


    /**
     * this si the read function for the datastore
     */
    private suspend fun read (key:String) : String?{
        val dataStoreKey= preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]

    }

    /**
     * this si the save function for the datastore
     */
    private suspend fun save (key:String , value:String){
        val dataStoreKey= preferencesKey<String>(key)
        dataStore.edit{temp ->
            temp[dataStoreKey]=value
        }
    }

    /**
     * this is to set the child inactive
     */
    fun setChildViewsInteractable(view: View, interactable: Boolean) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                child.isClickable = interactable
                child.isFocusable = interactable
                if (child is ViewGroup) {
                    setChildViewsInteractable(child, interactable)
                }
            }
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

/**
 * this is the retrofit instance that the object of the base url that is being called
 */
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