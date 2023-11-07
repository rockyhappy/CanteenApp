package com.example.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ApiService
import com.example.myapplication.R
import com.example.myapplication.RvModel
import com.example.myapplication.readFromDataStore
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Breakfast : Fragment(), RvAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var rvadapter : RvAdapter
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
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance2.getApiServiceWithToken(dataStore).getCanteens()
                if (response.isSuccessful) {
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Successful response: ${response.body()}")
                } else {
                    // Handle the error
                    Log.d("Testing",response.body().toString())
                    Log.d("Testing", "Response code: ${response.code()}, Response body: ${response.body()}")
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.d("Testing","Network Error")
            }
        }



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

/**
 * this is the interceptor to add the jwt to the header of the request
 */
class AuthInterceptor(private val jwtToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer $jwtToken")
        val request = requestBuilder.build()
        Log.d("JWT_TOKEN", jwtToken)
        return chain.proceed(request)
    }
}



/**
 * This is to the new api services that will give the jwt token
 */
object RetrofitInstance2 {
    private const val BASE_URL = "https://brunchbliss.onrender.com"

    // Removed the hard-coded JWT_TOKEN here

    private val authInterceptor = AuthInterceptor("")

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // Function to create the Retrofit API service with the JWT token
    private fun createApiService(jwtToken: String): ApiService {
        val authInterceptor = AuthInterceptor(jwtToken)
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    // Function to get the JWT token from DataStore
    suspend fun getApiServiceWithToken(dataStore: DataStore<Preferences>): ApiService {
        val jwtToken = readFromDataStore(dataStore, "token").toString()
        return createApiService(jwtToken)
    }
}

