package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.fragments.ChoiceFragment
import com.example.myapplication.fragments.ForgotPassward
import com.example.myapplication.fragments.LoginFragment

class Login : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginFragment=LoginFragment()
        val forgotPasswordFragment=ForgotPassward()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,ChoiceFragment())
            commit()
        }

    }
}