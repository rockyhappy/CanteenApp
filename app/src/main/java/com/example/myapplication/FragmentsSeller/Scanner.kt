

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
import androidx.fragment.app.Fragment
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView
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
    private lateinit var svgImageView2: SVGImageView
    private lateinit var scanButton: TextView
    private lateinit var verified :TextView
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
                Toast.makeText(context, content, Toast.LENGTH_LONG).show()
            }
        }
    }

}
