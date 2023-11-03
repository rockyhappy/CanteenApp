package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.myapplication.databinding.ActivityDashBoardBinding
import com.example.myapplication.fragments.ChoiceFragment
import com.example.myapplication.fragments.SettingsFragment
import com.google.android.material.navigation.NavigationView


class DashBoard : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityDashBoardBinding
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding=ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Setting up the Basic Fragment
         */
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, SettingsFragment())
            commit()
        }

        binding.apply{
            toggle = ActionBarDrawerToggle(this@DashBoard, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.firstItem -> {
                        Toast.makeText(this@DashBoard, "First Item Clicked", Toast.LENGTH_SHORT)
                            .show()
                    }

                    R.id.secondtItem -> {
                        Toast.makeText(this@DashBoard, "Second Item Clicked", Toast.LENGTH_SHORT)
                            .show()
                    }

                    R.id.thirdItem -> {
                        Toast.makeText(this@DashBoard, "third Item Clicked", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                true

            }
        }
        val bottomNavigation = findViewById<MeowBottomNavigation>(R.id.btmnav)

        val homeItem = MeowBottomNavigation.Model(1, R.drawable.baseline_home_24)
        val searchItem = MeowBottomNavigation.Model(2, R.drawable.ic_search)
        val profileItem = MeowBottomNavigation.Model(3, R.drawable.ic_profile)
        val settingsItem = MeowBottomNavigation.Model(4, R.drawable.ic_settings)


        bottomNavigation.add(homeItem)
        bottomNavigation.add(searchItem)
        bottomNavigation.add(profileItem)
        bottomNavigation.add(settingsItem)
        bottomNavigation.show(1, true)


        val topAppBar: Toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        supportActionBar?.title=""

        var drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this@DashBoard, drawerLayout, R.string.open, R.string.close)
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var navView :NavigationView =findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.firstItem -> {
                    Toast.makeText(this@DashBoard, "First Item Clicked", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, Dishes_Category())
                        commit()
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.secondtItem -> {
                    Toast.makeText(this@DashBoard, "Second Item Clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.thirdItem -> {
                    Toast.makeText(this@DashBoard, "third Item Clicked", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
}