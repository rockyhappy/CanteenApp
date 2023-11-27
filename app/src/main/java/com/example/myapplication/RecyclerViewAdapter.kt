import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.FoodItemCart
import com.example.myapplication.FoodItemCartDiffCallback
import com.example.myapplication.FoodItemWishlist
import com.example.myapplication.R
import com.example.myapplication.RvAdapterCart

class RvAdapterWishlist(
    var dataList: ArrayList<FoodItemWishlist>,
    var context : Context,
    private val itemClickListener: OnItemClickListener,
    private val cartClickListener: OnCartClickListener,
    private val wishClickListener: OnWishClickListener,
): RecyclerView.Adapter<RvAdapterWishlist.MyViewHolder>() {
    private var selectedPosition: Int = 0
    inner class MyViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        val cart =view.findViewById<Button>(R.id.cart)
        val wish =view.findViewById<ImageView>(R.id.wish)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.rv_item_wishlist, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var profile = holder.view.findViewById<ImageView>(R.id.imageview1)
        var price = holder.view.findViewById<TextView>(R.id.price)
        val itemName=holder.view.findViewById<TextView>(R.id.title)
        val ingredient=holder.view.findViewById<TextView>(R.id.ingredients)
        val rating=holder.view.findViewById<TextView>(R.id.ratings)


        val item = dataList[position]
        Glide.with(context)
            .load("https://i.postimg.cc/xTMVqcLJ/Break-fast.png")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.baseline_person_24)
                    .error(R.drawable.baseline_home_24)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profile)
        itemName.text=item.name
        ingredient.text=item.ingredients.toString()
        price.text="₹"+item.price.toInt().toString()+ " |"



        holder.cart.setOnClickListener {
            cartClickListener.onCartClick(item.id)
        }

        holder.wish.setOnClickListener {
            wishClickListener.onWishClick(item.id)
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
    interface OnCartClickListener{
        fun onCartClick(name: Long)
    }
    interface OnWishClickListener{
        fun onWishClick(name: Long)
    }
    fun removeItem(position: Int) {
        if (position in 0 until dataList.size) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateData(newDataList: List<FoodItemWishlist>) {
        //val diffResult = DiffUtil.calculateDiff(FoodItemCartDiffCallback(dataList, newDataList))
        dataList.clear()
        dataList.addAll(newDataList)
       // diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        notifyDataSetChanged() // Trigger a data set change to update the UI
    }
}
