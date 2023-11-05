package com.example.myapplication.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.datastore.core.DataStore
import androidx.datastore.createDataStore
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegistrationVerifyMail : Fragment(R.layout.fragment_registration_verify_mail) {
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
        val view = inflater.inflate(R.layout.fragment_registration_verify_mail, container, false)



        /** This is the BackButton */
        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, Registrationfragment())
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


        resendBtn.setOnClickListener {
            lifecycleScope.launch{
                val Email = readFromDataStore(dataStore,"Email" )
                val fullname = readFromDataStore(dataStore , "fullname")
                val resendOtp= resendOtpRequest(
                    email=Email.toString()
                )
                val response = RetrofitInstance.apiService.resendOtp(resendOtp)
                if(response.isSuccessful){
                    //showToast(response.body()?.token.toString())
                }
                else{
                    showToast("Something went Wrong")
                }
            }
            resendBtn.isEnabled=false
            startTimer()
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val container = view.findViewById<ConstraintLayout>(R.id.Container)
        /**This passes to the new Password*/
        val button= view.findViewById<Button>(R.id.button)
        button.setOnClickListener {

//            button.isEnabled = false
//            container.isEnabled = false
//            container.isFocusable = false
//            progressBar.visibility = View.VISIBLE

            var otp: String = editText1.text.toString()
            otp = otp + editText2.text.toString()
            otp = otp + editText3.text.toString()
            otp = otp + editText4.text.toString()
            otp = otp + editText5.text.toString()
            otp = otp + editText6.text.toString()

            if (otp.length == 6) {
                lifecycleScope.launch {
                    showCustomProgressDialog()
                    try {

                        val Email = readFromDataStore(dataStore, "Email")
                        val verifyMailRequest = verifyMailRequest(Email.toString(), otp)
                        val response = RetrofitInstance.apiService.checkEmail(verifyMailRequest)
                        if (response.isSuccessful) {
                            //showToast(otp)
                            //if(response.body()?.token.toString()!="OTP Expired" || response.body()?.token.toString()!="Incorrect OTP"||response.body()?.token.toString()!="No OTP generated"){
                            if (response.body()?.token.toString().length >= 20 && response.code().toString()=="201") {
                                save("token", response.body()?.token.toString())
                                val fragmentTransaction = parentFragmentManager.beginTransaction()
                                fragmentTransaction.replace(
                                    R.id.flFragment,
                                    Congratulationsfragment()
                                )
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            } else {
                                val incorrectOtp = view.findViewById<TextView>(R.id.incorrectOtp)
                                incorrectOtp.text = response.body()?.message.toString()
                            }
                        } else {
                            dismissCustomProgressDialog()
                            showToast("Wrong Otp")
                        }
                    } catch (e: Exception) {
                        showToast("Connection Error")
                    } finally {

//                        // Enable the button
//                        button.isEnabled = true
//                        container.isEnabled = true
//                        container.isFocusable = true
//                        progressBar.visibility = View.GONE
                        dismissCustomProgressDialog()
                    }
                }
            } else {
                dismissCustomProgressDialog()
                val incorrectOtp = view.findViewById<TextView>(R.id.incorrectOtp)
                incorrectOtp.text = "Fill correctly"
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
