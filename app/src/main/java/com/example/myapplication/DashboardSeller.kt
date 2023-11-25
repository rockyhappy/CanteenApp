package com.example.myapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.databinding.ActivityDashBoardBinding
import com.example.myapplication.databinding.ActivityDashboardSellerBinding

class DashboardSeller : AppCompatActivity() {


    private lateinit var binding: ActivityDashboardSellerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashboardSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        }
    }


