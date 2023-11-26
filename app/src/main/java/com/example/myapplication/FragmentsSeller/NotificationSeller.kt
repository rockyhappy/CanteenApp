package com.example.myapplication.FragmentsSeller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RvAdapter
import com.example.myapplication.sellerAdapter.notificationAdapter


class NotificationSeller : Fragment(), notificationAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : notificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_notification_seller, container, false)
        rvadapter = notificationAdapter(arrayListOf(1,2,3,4,5), requireContext(), this)
        recyclerView = view.findViewById(R.id.rvid)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = rvadapter


        return view
    }

    override fun onItemClickCanteen() {

    }

}