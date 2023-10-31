package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.myapplication.SignUpRequest
import com.example.myapplication.readFromDataStore
import com.example.myapplication.resendOtpRequest
import com.example.myapplication.verifyMailRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern


class VerifyMail : Fragment(R.layout.fragment_verify_mail) {


    private lateinit var dataStore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = requireContext().createDataStore(name = "user")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_verify_mail, container, false)

        /** This is the BackButton */
        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ForgotPassward())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        /**This si the svg main Image*/
        val svgImageView2 = view.findViewById<SVGImageView>(R.id.svgImageView2)
        var svg2 = SVG.getFromResource(resources, R.raw.otp_verification)
        svgImageView2.setSVG(svg2)

        /**This is the edit text for the otp*/
        var editText1=view.findViewById<EditText>(R.id.editText1)
        var editText2=view.findViewById<EditText>(R.id.editText2)
        var editText3=view.findViewById<EditText>(R.id.editText3)
        var editText4=view.findViewById<EditText>(R.id.editText4)
        var editText5=view.findViewById<EditText>(R.id.editText5)
        var editText6=view.findViewById<EditText>(R.id.editText6)


        editText1.addTextChangedListener(GenericTextWatcher(editText1, editText2))
        editText2.addTextChangedListener(GenericTextWatcher(editText2, editText3))
        editText3.addTextChangedListener(GenericTextWatcher(editText3, editText4))
        editText4.addTextChangedListener(GenericTextWatcher(editText4, editText5))
        editText5.addTextChangedListener(GenericTextWatcher(editText5, editText6))
        editText6.addTextChangedListener(GenericTextWatcher(editText6, null))



        editText1.setOnKeyListener(GenericKeyEvent(editText1, null))
        editText2.setOnKeyListener(GenericKeyEvent(editText2, editText1))
        editText3.setOnKeyListener(GenericKeyEvent(editText3, editText2))
        editText4.setOnKeyListener(GenericKeyEvent(editText4,editText3))
        editText5.setOnKeyListener(GenericKeyEvent(editText5,editText4))
        editText6.setOnKeyListener(GenericKeyEvent(editText6,editText5))

        /**
         * Now this is the code for the resend button  */
        lateinit var cTimer: CountDownTimer

        val resendBtn = view.findViewById<Button>(R.id.resendBtn)
        fun startTimer() {
            cTimer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    resendBtn.text = "Resend OTP in : ${millisUntilFinished / 1000}"
                }

                override fun onFinish() {
                    resendBtn.text = "Re send OTP!"
                    resendBtn.isEnabled = true
                }
            }
            cTimer.start()
        }
        startTimer()

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val container = view.findViewById<ConstraintLayout>(R.id.Container)

        resendBtn.setOnClickListener {
            lifecycleScope.launch{
                try {


                    val Email = readFromDataStore(dataStore, "Email")
                    val fullname = readFromDataStore(dataStore, "fullname")
                    val password = readFromDataStore(dataStore, "password")
                    val resendOtp = resendOtpRequest(
                        email = Email.toString()
                    )
                    //val response = RetrofitInstance.apiService.fetchData(signUpRequest)
                    val response = RetrofitInstance.apiService.resendOtp(resendOtp)
                    if (response.isSuccessful) {
                        //showToast(response.body()?.token.toString())
                    } else {
                        showToast("Something went Wrong")
                    }
                }catch (e:Exception)
                {
                    showToast("Connection Error")
                }
            }
            resendBtn.isEnabled=false
            startTimer()
        }


        /**This passes to the new Password*/
        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {
            /**
             * this is the api call verify mail
             */

            button.isEnabled = false
            container.isEnabled=false
            container.isFocusable = false
            progressBar.visibility = View.VISIBLE
            var otp : String=editText1.text.toString()
            otp=otp+editText2.text.toString()
            otp=otp+editText3.text.toString()
            otp=otp+editText4.text.toString()
            otp=otp+editText5.text.toString()
            otp=otp+editText6.text.toString()
            //showToast(otp)
            lifecycleScope.launch{

                try {

                    val Email = readFromDataStore(dataStore, "Email")
                    val verifyMailRequest = verifyMailRequest(Email.toString(), otp)
                    val response = RetrofitInstance.apiService.resetPasswordCheckEmail(verifyMailRequest)
                    if (response.isSuccessful) {
                        //showToast(otp)
                        //if(response.body()?.token.toString()!="OTP Expired" || response.body()?.token.toString()!="Incorrect OTP"||response.body()?.token.toString()!="No OTP generated"){
                        if (response.body()?.token.toString()=="Otp Verified Successfully") {
                            save("token", response.body()?.token.toString())
                            val fragmentTransaction = parentFragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.flFragment, NewPassword())
                            fragmentTransaction.addToBackStack(null)
                            fragmentTransaction.commit()
                        } else {
                            val incorrectOtp = view.findViewById<TextView>(R.id.incorrectOtp)
                            incorrectOtp.text = response.body()?.token.toString()
                        }
                    } else {
                        showToast("Please Retry")
                    }
                }
                catch(e: Exception)
                {
                    showToast("Connection Error")
                }
                finally {
                    /** API testing */
                    // Enable the button
                    button.isEnabled = true
                    container.isEnabled=true
                    container.isFocusable = true
                    progressBar.visibility = View.GONE

                }
            }
            }

        return view
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

class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.editText1 && currentView.text.isEmpty()) {
            previousView!!.text = null
            previousView.requestFocus()
            return true
        }
        return false
    }


}

class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) {
        val text = editable.toString()
        when (currentView.id) {
            R.id.editText1 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.editText2 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.editText3 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.editText4 -> if (text.length == 1) nextView!!.requestFocus()
            R.id.editText5 -> if (text.length == 1) nextView!!.requestFocus()

        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) {
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) {

    }

}


