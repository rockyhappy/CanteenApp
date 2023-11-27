package com.example.myapplication

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.FragmentsSeller.MainDashboardSeller
import com.example.myapplication.FragmentsSeller.NotificationSeller
import com.example.myapplication.FragmentsSeller.OrderSeller
import com.example.myapplication.FragmentsSeller.PaymentSeller
import com.example.myapplication.FragmentsSeller.Scanner
import com.example.myapplication.databinding.ActivityDashBoardBinding
import com.example.myapplication.databinding.ActivityDashboardSellerBinding
import com.example.myapplication.fragments.Dishes_Category
import com.example.myapplication.fragments.MainDashboard
import com.example.myapplication.fragments.cart
import com.example.myapplication.fragments.wishlist
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardSeller : AppCompatActivity() {


    private lateinit var binding: ActivityDashboardSellerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDashboardSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, MainDashboardSeller())
            commit()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, MainDashboardSeller()).commit()
                    true
                }
                R.id.action_menu -> {
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, OrderSeller()).commit()
                    true
                }
                R.id.action_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, Scanner()).commit()
                    true
                }
                R.id.action_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, PaymentSeller()).commit()
                    true
                }
                else -> false
            }
        }

        }
    }


