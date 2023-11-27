

package com.example.myapplication.FragmentsSeller

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.google.zxing.Result
import com.example.myapplication.R
import com.example.myapplication.RetrofitInstance2
import com.example.myapplication.addCartItemsResponse
import com.google.zxing.ResultPoint
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.coroutines.launch

class Scanner : Fragment() {

    private val integrator by lazy {
        IntentIntegrator.forSupportFragment(this)
    }
    private lateinit var svgImageView2: SVGImageView
    private lateinit var scanButton: TextView
    private lateinit var verified :TextView
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)

        svgImageView2 = view.findViewById(R.id.svgImageView2)
        val svg2 = SVG.getFromResource(resources, R.raw.qr)
        svgImageView2.setSVG(svg2)
        verified=view.findViewById(R.id.verified)


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanButton = view.findViewById(R.id.scanButton)
        dataStore = requireContext().createDataStore(name = "user")
        integrator.setOrientationLocked(true)
        scanButton.setOnClickListener {
            integrator.initiateScan()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                val content = result.contents
                val newSvg = SVG.getFromResource(resources, R.raw.verified_order)
                svgImageView2.setSVG(newSvg)
                verified.visibility=View.VISIBLE
                scanButton.text="Go To Home Page >"
                showToast(content)
                //Toast.makeText(context, content, Toast.LENGTH_LONG).show()

//                lifecycleScope.launch{
//                    val request=addCartItemsResponse(message = content.toString())
//                    val response=RetrofitInstance2.getApiServiceWithToken(dataStore).qrScanner(request)
//                    if(response=="QR Code is valid")
//                        showToast("User Verified")
//                    else
//                        showToast("User Not Verified")
//                }
            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
