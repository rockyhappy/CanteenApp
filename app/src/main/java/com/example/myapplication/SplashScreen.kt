package com.example.myapplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import kotlinx.coroutines.launch


class SplashScreen : AppCompatActivity() {
    private lateinit var dataStore: DataStore<Preferences>
    private var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val svgImageView = findViewById<SVGImageView>(R.id.svgImageView)
        val svg = SVG.getFromResource(resources, R.raw.b)
        svgImageView.setSVG(svg)
        dataStore = createDataStore(name = "user")
        lifecycleScope.launch {
            showCustomProgressDialog()
            try {
                val token = readFromDataStore(dataStore, "token" ).toString()
                if (token == "null")
                {
                    val intent= Intent(this@SplashScreen,Login::class.java)
                    startActivity(intent)
                    finish()
                }
                else {

                    //over here i can add an api to check that if the added token is verified or not

                    val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteens()
                    if (response.isSuccessful) {
                        val intent= Intent(this@SplashScreen,DashBoard::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val intent= Intent(this@SplashScreen,Login::class.java)
                        startActivity(intent)
                        finish()
                        Log.d("Testing",response.body().toString())
                        Log.d("Testing", "Response code: ${response.code()}, Response body: ${response.body()}")
                    }
                }
            }catch (e: Exception){
                showToast("SomeThing went wrong")
            }
            finally {
                dismissCustomProgressDialog()
            }
           dismissCustomProgressDialog()


        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this@SplashScreen, message, Toast.LENGTH_SHORT).show()
    }
    private fun showCustomProgressDialog() {
        dialog = Dialog(this)
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