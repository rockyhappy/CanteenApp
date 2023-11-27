package com.example.myapplication

import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
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
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.RatingBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.fragments.SearchFilter
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            itemClickListener.onItemClickCanteen(item.name)
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
        fun onItemClickCanteen(name: String)
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
    var dataList: ArrayList<FoodItem>,
    var context : Context,
    private val itemClickListener: OnItemClickListener,
    private val cartClickListener: OnCartClickListener,
    private val wishClickListener: OnWishClickListener
): RecyclerView.Adapter<RvAdapter2.MyViewHolder>() {
    private var selectedPosition: Int = 0
    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        val cart =view.findViewById<Button>(R.id.cart)
        val wish : FloatingActionButton=view.findViewById(R.id.wish)
    }

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
        var rate=holder.view.findViewById<RatingBar>(R.id.rate)
        var ratings=holder.view.findViewById<TextView>(R.id.ratings)

        val item = dataList[position]
        name.text = item.name
        residence.text = item.price.toString()
        rate.rating=item.averageRating.toFloat()
        ratings.text="("+item.noOfRatings+" ratings)"
        Glide.with(context)
            .load("https://i.postimg.cc/xTMVqcLJ/Break-fast.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_home_24)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profile)


        holder.cart.setOnClickListener {
            cartClickListener.onCartClick(item.id,false)
            //holder.cart.text="GoTo"
        }
        if(item.isInWishlist)
        {
            holder.wish.setImageResource(R.drawable.heart_filled)
        }
        if(item.isInCart)
        {
            //holder.cart.isEnabled=false
            holder.cart.text="GoTo"
            holder.cart.setOnClickListener {
                cartClickListener.onCartClick(item.id,true)
            }
            //holder.cart.isEnabled=true
        }
        holder.wish.setOnClickListener {
            //holder.wish.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN)
            holder.wish.isEnabled=false
            wishClickListener.onWishClick(item.id, item.isInWishlist)
            holder.wish.isEnabled=true
        }

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
    interface OnCartClickListener {
        fun onCartClick(name: Long,isIn :Boolean)
    }
    interface OnWishClickListener {
        fun onWishClick(name: Long,isIn: Boolean)
    }
    fun updateData(newDataList: List<FoodItem>) {
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
    var dataList: ArrayList<FoodItemCart>,
    var context : Context,
    private val itemClickListener: OnItemClickListener,
    private val itemDeleteListener: OnDeleteClickListener,
    private val quantityIncreaseListener: RvAdapterCart.OnQuantityIncreaseListener,
    private val quantityDecreaseListener: RvAdapterCart.OnQuantityDecreaseListener
): RecyclerView.Adapter<RvAdapterCart.MyViewHolder>() {
    private var selectedPosition: Int = 0
    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        val delete =view.findViewById<ImageView>(R.id.delete)
        val qunatity=view.findViewById<TextView>(R.id.quantity)
        val minus=view.findViewById<ImageButton>(R.id.minus)
        val plus=view.findViewById<ImageButton>(R.id.plus)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.rv_item_cart, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var profile = holder.view.findViewById<ImageView>(R.id.imageView1)
        var name = holder.view.findViewById<TextView>(R.id.textView1)
        var residence = holder.view.findViewById<TextView>(R.id.price)
        var quantity =holder.view.findViewById<TextView>(R.id.quantity)

        var item = dataList[position]
        name.text = item.foodItemName
        residence.text=item.price.toString()
        quantity.text=item.quantity.toString()
        Glide.with(context)
            .load("https://i.postimg.cc/xTMVqcLJ/Break-fast.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_home_24)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profile)


        holder.plus.setOnClickListener {
            quantityIncreaseListener.onPlusClick(position ,item.foodItemId, item.price.toDouble())
            increaseQuantity(position)
        }
        holder.minus.setOnClickListener {
            quantityDecreaseListener.onMinusClick(position,item.foodItemId,item.price.toDouble(),item.quantity)
            decreaseQuantity(position)
        }
        holder.delete.setOnClickListener {
            itemDeleteListener.onDeleteClick(item.foodItemId)
        }

        holder.view.setOnClickListener {
            itemClickListener.onItemClick(item.foodItemId)
        }
        if (position == selectedPosition) {
            //holder.dotImageView.setBackgroundResource(R.drawable.dot_selected)
        } else {
            //holder.dotImageView.setBackgroundResource(R.drawable.dot_unselected)
        }

    }

    fun increaseQuantity(position: Int) {
        if (position in 0 until dataList.size) {
            val currentItem = dataList[position]
            val updatedItem = currentItem.copy(quantity = currentItem.quantity + 1)
            dataList[position] = updatedItem
            notifyItemChanged(position)
        }
    }

    fun decreaseQuantity(position: Int) {
        if (position in 0 until dataList.size) {
            val currentItem = dataList[position]
            if (currentItem.quantity > 1) {
                val updatedItem = currentItem.copy(quantity = currentItem.quantity - 1)
                dataList[position] = updatedItem
                notifyItemChanged(position)
            }
        }
    }

    /**
     * This is for the click in the recycler view
     */
    interface OnItemClickListener {
        fun onItemClick(name: Long)
    }
    interface OnDeleteClickListener {
        fun onDeleteClick(name: Long)
    }

    interface OnQuantityDecreaseListener {
        fun onMinusClick(position:Int,name: Long,quanti :Double, quantity: Int)
    }
    interface OnQuantityIncreaseListener {
        fun onPlusClick(position:Int,name: Long,quantity:Double)
    }
    fun removeItem(position: Int) {
        if (position in 0 until dataList.size) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateData(newDataList: List<FoodItemCart>) {
        //val diffResult = DiffUtil.calculateDiff(FoodItemCartDiffCallback(dataList, newDataList))

        dataList?.let {
            // Update your adapter data here
            dataList.clear()
            dataList.addAll(newDataList)
        }

        //diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Trigger a data set change to update the UI
    }
}


class FoodItemCartDiffCallback(
    private val oldList: List<FoodItemCart>,
    private val newList: List<FoodItemCart>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

/**
 * This is the adapter for the search Filter
 */

class RvAdapterSearch(
    var dataList: ArrayList<RvModel>,
    var context : Context,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<RvAdapterSearch.MyViewHolder>() {
    private var selectedPosition: Int = 0


    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        val name: Button = view.findViewById(R.id.breakfast)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.rv__button_search , parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList[position]
        holder.name.text = item.name

        if (holder.adapterPosition == selectedPosition) {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.primary_color))
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rounded_border2)
        } else {
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.grey))
            holder.name.background = ContextCompat.getDrawable(context, R.drawable.rounded_border)
        }

        holder.name.setOnClickListener {
            selectedPosition = holder.adapterPosition
//            if (holder.adapterPosition == selectedPosition) {
//                holder.name.setTextColor(ContextCompat.getColor(context, R.color.primary_color))
//                holder.name.background = ContextCompat.getDrawable(context, R.drawable.rounded_border2)
//            } else {
//                holder.name.setTextColor(ContextCompat.getColor(context, R.color.grey))
//                holder.name.background = ContextCompat.getDrawable(context, R.drawable.rounded_border)
//            }
            notifyDataSetChanged()
            itemClickListener.onItemClick(item.name)
        }

        holder.view.setOnClickListener {
            selectedPosition = holder.adapterPosition
//            if (holder.adapterPosition == selectedPosition) {
//                holder.name.setTextColor(ContextCompat.getColor(context, R.color.primary_color))
//                holder.name.background = ContextCompat.getDrawable(context, R.drawable.rounded_border2)
//            } else {
//                holder.name.setTextColor(ContextCompat.getColor(context, R.color.grey))
//                holder.name.background = ContextCompat.getDrawable(context, R.drawable.rounded_border)
//            }
            notifyDataSetChanged()
            Log.d("RvAdapterSearch", "Item clicked: ${item.name}")
            itemClickListener.onItemClick(item.name)
        }


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
