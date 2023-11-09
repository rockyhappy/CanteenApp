package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.myapplication.databinding.ActivityDashBoardBinding
import com.example.myapplication.fragments.Breakfast
import com.example.myapplication.fragments.Dishes_Category
import com.example.myapplication.fragments.MainDashboard
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch


class DashBoard : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityDashBoardBinding
    private lateinit var dataStore: DataStore<Preferences>
    override fun onCreate(savedInstanceState: Bundle?) {
        dataStore = createDataStore(name = "user")

        super.onCreate(savedInstanceState)
        binding=ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Setting up the Basic Fragment
         */
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, MainDashboard())
            commit()
        }

        binding.apply{
            toggle = ActionBarDrawerToggle(this@DashBoard, drawerLayout, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.hide()

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
        val menu = MeowBottomNavigation.Model(2, R.drawable.baseline_restaurant_menu_24)
        val searchItem = MeowBottomNavigation.Model(3, R.drawable.baseline_bookmark_border_24)
        val profileItem = MeowBottomNavigation.Model(4, R.drawable.baseline_shopping_cart_24)
        val settingsItem = MeowBottomNavigation.Model(5, R.drawable.ic_profile)



        bottomNavigation.add(homeItem)
        bottomNavigation.add(menu)
        bottomNavigation.add(searchItem)
        bottomNavigation.add(profileItem)
        bottomNavigation.add(settingsItem)
        bottomNavigation.show(1, true)


        //val topAppBar: Toolbar = findViewById(R.id.topAppBar)
        //setSupportActionBar(topAppBar)
        //supportActionBar?.title=""

        var drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this@DashBoard, drawerLayout, R.string.open, R.string.close)
        //toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white))
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var navView :NavigationView =findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.firstItem -> {
                    Toast.makeText(this@DashBoard, "First Item Clicked", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        val e=readFromDataStore(dataStore,"Email")
                        showToast(e.toString())
                    }

                    supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, Dishes_Category())
                        commit()
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.secondtItem -> {
                    Toast.makeText(this@DashBoard, "Second Item Clicked", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, Breakfast())
                        commit()
                    }
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.thirdItem -> {
                    lifecycleScope.launch{
                        save("token","null")
                        startActivity(Intent(this@DashBoard,Login::class.java))
                    }
                    Toast.makeText(this@DashBoard, "third Item Clicked", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        /**
         * This is working when the item  is not selected
         */
        bottomNavigation.setOnClickMenuListener { item ->

            when (item.id) {
                1 -> {
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, MainDashboard())
                        commit()
                    }
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
        /**
         * This is working when the item  is selected and then we are re-selecting the same item
         */
        bottomNavigation.setOnReselectListener {item ->
            when (item.id) {
                1 -> {
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, MainDashboard())
                        commit()
                    }
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

    /**
     * This function Enables the opening and closing of the drawer
     */
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (toggle.onOptionsItemSelected(item)){
//            true
//        }
//        return super.onOptionsItemSelected(item)
//    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private suspend fun save (key:String , value:String){
        val dataStoreKey= preferencesKey<String>(key)
        dataStore.edit{temp ->
            temp[dataStoreKey]=value
        }
    }
}