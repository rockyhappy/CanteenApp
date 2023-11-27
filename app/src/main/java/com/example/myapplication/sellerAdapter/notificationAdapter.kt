package com.example.myapplication.sellerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.RvAdapter
import com.example.myapplication.RvModel

class notificationAdapter(
    var dataList: ArrayList<Int>,
    var context : Context,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<notificationAdapter.MyViewHolder>() {
    private var selectedPosition: Int = 0
    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.rv_item_seller_notification , parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size*3
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        var profile = holder.view.findViewById<ImageView>(R.id.imageView)
//        var name = holder.view.findViewById<TextView>(R.id.textView)
//        //var residence = holder.view.findViewById<TextView>(R.id.textView2)
//
        val item = dataList[position % dataList.size]
//        name.text = item.name
//
//        Glide.with(context)
//            .load("https://i.postimg.cc/Vkt4HFqH/726617d5b82c0367ff5faadb547da306.png")
//            .apply(
//                RequestOptions()
//                    .placeholder(R.drawable.baseline_person_24)
//                    .error(R.drawable.baseline_home_24)
//            )
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .into(profile)
        //var dotImageView = holder.view.findViewById<ImageView>(R.id.dotImageView)


        holder.view.setOnClickListener {
            itemClickListener.onItemClickCanteen()
        }
//        if (position == selectedPosition) {
//            dotImageView.setBackgroundResource(R.drawable.dot_selected)
//        } else { dotImageView.setBackgroundResource(R.drawable.dot_unselected)
//        }

    }
    /**
     * This is for the click in the recycler view
     */
    interface OnItemClickListener {
        fun onItemClickCanteen()
    }


    fun updateData(newDataList: List<Int>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Trigger a data set change to update the UI
    }
    fun updateDataWithDiffUtil(newDataList: List<Int>) {
        val diffResult = DiffUtil.calculateDiff(NotificationDiffCallback(dataList, newDataList))
        dataList.clear()
        dataList.addAll(newDataList)
        diffResult.dispatchUpdatesTo(this)
    }
}
class NotificationDiffCallback(
    private val oldList: List<Int>,
    private val newList: List<Int>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]== newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // You can compare other properties if needed
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
