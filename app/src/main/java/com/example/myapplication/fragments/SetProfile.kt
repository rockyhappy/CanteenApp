package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.SetProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class SetProfile : Fragment() {

    private lateinit var editTextFullName: EditText
    private lateinit var editTextProfileImage: EditText
    private lateinit var editTextContactNumber: EditText
    private lateinit var imageViewProfile: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_set_profile, container, false)

        editTextFullName = view.findViewById(R.id.editText)
        editTextProfileImage = view.findViewById(R.id.editext8)
        editTextContactNumber = view.findViewById(R.id.editText7)
        imageViewProfile = view.findViewById(R.id.imagView20)

        // Fetch and populate profile data
        fetchProfileData()

        return view
    }
    
    private fun fetchProfileData() {
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response: Response<SetProfileResponse> = RetrofitInstance.apiService.SetProfile()

                    if (response.isSuccessful) {
                        val setProfileResponse: SetProfileResponse? = response.body()
                        // Update UI with the retrieved data
                        updateUI(setProfileResponse)
                    } else {
                        // Handle API error
                        showToast("Error: ${response.code()}: ${response.message()}")
                    }
                } catch (e: Exception) {
                    // Handle exceptions (e.g., network issues)
                    showToast("Exception: ${e.message}")
                }
            }
        }
    }

    private fun updateUI(profileResponse: SetProfileResponse?) {
        requireActivity().runOnUiThread {
            editTextFullName.setText(profileResponse?.fullName)
            editTextProfileImage.setText(profileResponse?.profileImage)
            editTextContactNumber.setText(profileResponse?.contactNumber)

            // Load profile image using Glide (or your preferred image loading library)
            Glide.with(this@SetProfile)
                .load(profileResponse?.profileImage)
                .into(imageViewProfile)
        }
    }

    private fun showToast(message: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}