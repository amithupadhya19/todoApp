package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.ActivityMainBinding.inflate
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class AddCategoryDialogFragment( drawerList1: ArrayList<String>,
                                val adapter:CategoryAdapter,
                                 spinnerList1:ArrayList<category>,
                                 val ThisContext:Context,
                                 var username:String
):DialogFragment() {
    var drawerList=drawerList1
    var spinnerList=spinnerList1
    val baseURL="https://todoapp1900.herokuapp.com/"
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView:View=inflater.inflate(R.layout.add_category_dialog,container,false)
        rootView.findViewById<Button>(R.id.dialog_add).setOnClickListener {
            var s= rootView.findViewById<EditText>(R.id.category_dialog_et).text.toString().toLowerCase()
            if (drawerList.contains(s)){
               Toast.makeText(ThisContext,"category "+s+" already exists",Toast.LENGTH_LONG).show()
            }
            else {
                val newCategory = category(s)
                drawerList.add(drawerList.size, s)
                //add category to mysql db
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val retrofitBuilder= Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(baseURL)
                    .build()
                    .create(apiInterface::class.java)
                val postTask=retrofitBuilder.postcategory("$s",username)
                postTask.enqueue(object : Callback<category?> {
                    override fun onResponse(call: Call<category?>, response: Response<category?>) {
                        print("on success"+response.body())
                    }

                    override fun onFailure(call: Call<category?>, t: Throwable) {
                        print("on failure "+t.message)
                    }
                })
                adapter.notifyItemInserted(drawerList.size)
                dismiss()
                spinnerList.add(newCategory)
            }

        }
        return rootView
    }
}