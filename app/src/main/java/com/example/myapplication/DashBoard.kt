package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.etebarian.meowbottomnavigation.MeowBottomNavigation



class DashBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.btmnav)

        val homeItem = MeowBottomNavigation.Model(1, R.drawable.baseline_home_24)
        val searchItem = MeowBottomNavigation.Model(2, R.drawable.ic_search)
        val profileItem = MeowBottomNavigation.Model(3, R.drawable.ic_profile)
        val settingsItem = MeowBottomNavigation.Model(4, R.drawable.ic_settings)


        bottomNavigation.add(homeItem)
        bottomNavigation.add(searchItem)
        bottomNavigation.add(profileItem)
        bottomNavigation.add(settingsItem)
        bottomNavigation.show(1, true) // Adjust the icon scaling as needed


        bottomNavigation.setOnClickMenuListener { item ->
            when (item.id) {
                1 -> {
                    // Handle the "Home" tab selection
                }
                2 -> {
                    // Handle the "Search" tab selection
                }
                3 -> {
                    // Handle the "Profile" tab selection
                }
                4 -> {
                    // Handle the "Settings" tab selection
                }
            }
        }



    }
}