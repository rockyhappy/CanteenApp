package com.example.myapplication.FragmentsSeller

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.Dishes_Category
import com.example.myapplication.sellerAdapter.PaymentAdapter
import com.example.myapplication.sellerAdapter.orderAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainDashboardSeller : Fragment() ,PaymentAdapter.OnItemClickListener,orderAdapter.OnItemClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : PaymentAdapter

    private lateinit var recyclerViewOrder: RecyclerView
    private lateinit var rvadapterOrder : orderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_main_dashboard_seller, container, false)

        rvadapter = PaymentAdapter(arrayListOf(1,2,3,4,5), requireContext(), this)
        recyclerView = view.findViewById(R.id.rvid)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2,GridLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = rvadapter

        rvadapterOrder = orderAdapter(arrayListOf(1,2,3,4,5), requireContext(), this)
        recyclerViewOrder = view.findViewById(R.id.rvi)
        recyclerViewOrder.layoutManager = GridLayoutManager(requireContext(), 3,GridLayoutManager.HORIZONTAL, false)
        recyclerViewOrder.adapter = rvadapterOrder

        val notification=view.findViewById<ImageView>(R.id.imageView6)
        notification.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, NotificationSeller())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val payment=view.findViewById<TextView>(R.id.id1)
        payment.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, PaymentSeller())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val order=view.findViewById<TextView>(R.id.id2)
        order.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragment, OrderSeller())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }

    override fun onItemClickCanteen() {

    }

}