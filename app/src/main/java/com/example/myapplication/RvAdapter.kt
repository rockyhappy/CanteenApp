package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import android.graphics.Rect

/**
 * This is the adapter that is loading the canteens in the home page
 */

class RvAdapter(
    var dataList: ArrayList<RvModel>,
    var context : Context,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<RvAdapter.MyViewHolder>() {
    private var selectedPosition: Int = 0
    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.rv_item_breakfast , parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var profile = holder.view.findViewById<ImageView>(R.id.imageView)
        var name = holder.view.findViewById<TextView>(R.id.textView)
        //var residence = holder.view.findViewById<TextView>(R.id.textView2)

        val item = dataList[position]
        name.text = item.name
        //residence.text = item.descriptionn

        Glide.with(context)
            .load("https://i.postimg.cc/Vkt4HFqH/726617d5b82c0367ff5faadb547da306.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_home_24)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profile)
        //var dotImageView = holder.view.findViewById<ImageView>(R.id.dotImageView)


        holder.view.setOnClickListener {
            itemClickListener.onItemClick(item.name)
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
        fun onItemClick(name: String)
    }


    fun updateData(newDataList: List<RvModel>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Trigger a data set change to update the UI
    }
}

/**
 * This is the recycler view adapter for the showing of items
 */
class RvAdapter2(
    var dataList: ArrayList<RvModel2>,
    var context : Context,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<RvAdapter2.MyViewHolder>() {
    private var selectedPosition: Int = 0
    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.rv_item_view, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var profile = holder.view.findViewById<ImageView>(R.id.imageView1)
        var name = holder.view.findViewById<TextView>(R.id.textView1)
        var residence = holder.view.findViewById<TextView>(R.id.price)


        val item = dataList[position]
        name.text = item.name
        residence.text = item.price

        Glide.with(context)
            .load("https://i.postimg.cc/xTMVqcLJ/Break-fast.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_home_24)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profile)


        holder.view.setOnClickListener {
            itemClickListener.onItemClick(item.id)
        }
        if (position == selectedPosition) {
            //holder.dotImageView.setBackgroundResource(R.drawable.dot_selected)
        } else {
            //holder.dotImageView.setBackgroundResource(R.drawable.dot_unselected)
        }

    }
    /**
     * This is for the click in the recycler view
     */
    interface OnItemClickListener {
        fun onItemClick(name: Long)
    }


    fun updateData(newDataList: List<RvModel2>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Trigger a data set change to update the UI
    }
}


class SpaceItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)

        // Apply the margin to all items except the last one
        if (itemPosition < parent.adapter?.itemCount?.minus(1) ?: 0) {
            outRect.bottom = spaceHeight
        }
    }
}


/**
 * This is the adapter for the cart page
 */


class RvAdapterCart(
    var dataList: ArrayList<RvModel2>,
    var context : Context,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<RvAdapterCart.MyViewHolder>() {
    private var selectedPosition: Int = 0
    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.rv_item_view, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var profile = holder.view.findViewById<ImageView>(R.id.imageView1)
        var name = holder.view.findViewById<TextView>(R.id.textView1)
        var residence = holder.view.findViewById<TextView>(R.id.price)


        val item = dataList[position]
        name.text = item.name
        residence.text = item.price

        Glide.with(context)
            .load("https://i.postimg.cc/xTMVqcLJ/Break-fast.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_home_24)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profile)


        holder.view.setOnClickListener {
            itemClickListener.onItemClick(item.id)
        }
        if (position == selectedPosition) {
            //holder.dotImageView.setBackgroundResource(R.drawable.dot_selected)
        } else {
            //holder.dotImageView.setBackgroundResource(R.drawable.dot_unselected)
        }

    }
    /**
     * This is for the click in the recycler view
     */
    interface OnItemClickListener {
        fun onItemClick(name: Long)
    }


    fun updateData(newDataList: List<RvModel2>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Trigger a data set change to update the UI
    }
}
