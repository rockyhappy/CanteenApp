package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityDashBoardBinding
import com.example.myapplication.fragments.Breakfast
import com.example.myapplication.fragments.Dishes_Category
import com.example.myapplication.fragments.MainDashboard
import com.example.myapplication.fragments.QRcode
import com.example.myapplication.FragmentsSeller.Scanner
import com.example.myapplication.fragments.cart
import com.example.myapplication.fragments.wishlist
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.launch


class DashBoard : AppCompatActivity(), PaymentResultListener {
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


        var drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        toggle = ActionBarDrawerToggle(this@DashBoard, drawerLayout, R.string.open, R.string.close)
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


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    // Handle home action
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, MainDashboard()).commit()
                    true
                }
                R.id.action_menu -> {
                    // Handle menu action
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, Dishes_Category()).commit()
                    true
                }
                R.id.action_search -> {
                    // Handle search action
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, wishlist()).commit()
                    // Add your logic for handling search
                    true
                }
                R.id.action_cart -> {
                    // Handle cart action
                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, cart()).commit()
                    true
                }
                R.id.action_profile -> {

                    supportFragmentManager.beginTransaction().replace(R.id.flFragment, Scanner()).commit()


                    lifecycleScope.launch {
                        try{
                            save("token","null")
                            startActivity(Intent(this@DashBoard, Login::class.java))
                            finish()
                        }catch (e:Exception){ }finally {}
                    }
                    true
                }
                else -> false
            }
        }



    }

    /**
     * This function Enables the opening and closing of the drawer
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private suspend fun save (key:String , value:String){
        val dataStoreKey= preferencesKey<String>(key)
        dataStore.edit{temp ->
            temp[dataStoreKey]=value
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {

        /**
         * Payment Gateway razorPaymentId
         */
        Log.d("RazorPay",razorpayPaymentId.toString())
        val totalAmount=cart.totalAmount
        lifecycleScope.launch {
            try {
                val request=PaymentInfo2(
                    paymentId = razorpayPaymentId.toString(),
                    amount = totalAmount
                )
                val response=RetrofitInstance2.getApiServiceWithToken(dataStore).capturePayment(request)
                if(response.isSuccessful)
                {
                    showToast("Order in Process")
                    save("qr",razorpayPaymentId.toString())
                    val bundle = Bundle()
                    bundle.putString("key1", razorpayPaymentId.toString())
                    val passing = QRcode()
                    passing.arguments = bundle
                    showToast("Order in prepration")
                    supportFragmentManager.popBackStack()
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, passing)
                        commit()
                    }
                }
                else {
                    showToast("Payment not verified")
                }
            }catch(e: Exception)
            {

            }
            finally {
                try {
                    val qr=RetrofitInstance2.getApiServiceWithToken(dataStore).qrGenerate()
                    if(qr!=null) {
                        save("qr",qr)
                        Log.d("test",qr)
                        val bundle = Bundle()
                        bundle.putString("key1", qr)
                        val passing = QRcode()
                        passing.arguments = bundle
                        showToast("Order in prepration")
                        supportFragmentManager.popBackStack()
                        supportFragmentManager.beginTransaction().apply {
                            replace(R.id.flFragment, passing)
                            commit()
                        }
                    }
                }catch (e :Exception){

                }finally{

                }
            }


        }

        //showToast("$razorpayPaymentId")

    }

    override fun onPaymentError(code: Int, response: String?) {
        // Handle payment failure
        //showToast("Payment Failed. Code: $code, Response: $response")
    }
}
