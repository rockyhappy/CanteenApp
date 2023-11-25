

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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.google.zxing.Result
import com.example.myapplication.R
import com.google.zxing.ResultPoint
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity

class Scanner : Fragment() {

    private val integrator by lazy {
        IntentIntegrator.forSupportFragment(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scanner, container, false)



        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scanButton: Button = view.findViewById(R.id.scanButton)
        integrator.setOrientationLocked(true)
        // Set a click listener for the button
//        scanButton.setOnClickListener {
//            val intent = Intent(context, CaptureActivity::class.java)
//            intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // You can customize this based on your needs
//            startActivityForResult(intent, 123)
//        }
//        val scanButton: Button = view.findViewById(R.id.scanButton)

        // Set a click listener for the button
        scanButton.setOnClickListener {
            // Start the scan
            integrator.initiateScan()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        // Handle the scan result
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                // Get the QR code content
                val content = result.contents

                // Display the QR code content
                Toast.makeText(context, content, Toast.LENGTH_LONG).show()
            }
        }
    }

}
