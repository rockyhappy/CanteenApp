package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RvModel


class Breakfast : Fragment(), RvAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_breakfast, container, false)
        recyclerView=view.findViewById<RecyclerView>(R.id.rvid)
        rvadapter= RvAdapter(RvDataList.getData(),requireContext(),this)
        recyclerView.layoutManager= GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
        recyclerView.adapter= rvadapter


        return view
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClick(name: String) {
        showToast(name)

    }

}


class RvAdapter(
    var dataList: ArrayList<RvModel>,
    var context : Context,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =LayoutInflater.from(context).inflate(R.layout.rv_item_breakfast , parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var profile = holder.view.findViewById<ImageView>(R.id.imageView)
        var name = holder.view.findViewById<TextView>(R.id.textView)
        var residence = holder.view.findViewById<TextView>(R.id.textView2)
        profile.setImageResource(dataList.get(position).profile)
        name.text =dataList.get(position).name
        residence.text=dataList.get(position).adress

        val item = dataList[position]
        profile.setImageResource(item.profile)
        name.text = item.name
        residence.text = item.adress


        holder.view.setOnClickListener {
            // Call the onItemClick function with the name of the clicked item
            itemClickListener.onItemClick(item.name)
        }

    }
    /**
     * This is for the click in the recycler view
     */
    interface OnItemClickListener {
        fun onItemClick(name: String)
    }

}


object RvDataList {
    private lateinit var datalist: ArrayList<RvModel>
    fun getData():ArrayList<RvModel>{

        datalist=ArrayList<RvModel>()
        datalist.add(RvModel(R.drawable.breakfast,"Rachit Katiyar","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Rachit Katiyar","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Rachit Katiyar","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Rachit Katiyar","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Rachit Katiyar","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Ujjwal Rana","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Andrew Tate","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"jimmy parson","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Paras Upadhyay","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Ankit Varshney","Software Incubator"))
        datalist.add(RvModel(R.drawable.breakfast,"Lakshay Gupta","Software Incubator"))
        return datalist
    }
}