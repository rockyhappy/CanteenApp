package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGImageView


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val svgImageView = findViewById<SVGImageView>(R.id.svgImageView)
        val svg = SVG.getFromResource(resources, R.raw.b)
        svgImageView.setSVG(svg)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                val intent= Intent(this,DashBoard::class.java)
                startActivity(intent)
                finish()
            },2000
        )
    }
}