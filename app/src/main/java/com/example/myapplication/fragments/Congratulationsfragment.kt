package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.DashBoard
import com.example.myapplication.DashboardSeller
import com.example.myapplication.R
import com.example.myapplication.readFromDataStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class Congratulationsfragment : Fragment(R.layout.fragment_congratulationsfragment) {
    private lateinit var dataStore: DataStore<Preferences>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStore = requireContext().createDataStore(name = "user")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_congratulationsfragment, container, false)

        val backButton: FloatingActionButton =view.findViewById(R.id.backButton)
        backButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, ChoiceFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val button=view.findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            lifecycleScope.launch {
                if(readFromDataStore(dataStore,"role")=="USER"){
                    startActivity(Intent(requireActivity(), DashBoard::class.java))
                    requireActivity().finish()
                }
                else{
                    startActivity(Intent(requireActivity(), DashboardSeller::class.java))
                    requireActivity().finish()
                }
            }
        }


        return view
    }

}