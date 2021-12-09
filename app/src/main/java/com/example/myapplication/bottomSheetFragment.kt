package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.myapplication.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Request


class BottomSheetFragment(arrayList: ArrayList<task>, adapter: Adapter,secondArray:ArrayList<String>,string:String
,var username:String): BottomSheetDialogFragment(){
    var _binding1:BottomSheetBinding?=null
    var s1=string
    val binding1 get() = _binding1!!
    var tasks=arrayList
    var adapterThis=adapter
    var categoriess=secondArray
    var s:String="choose a category"
    val baseURL="https://todoapp1900.herokuapp.com/"
    lateinit var ettd:EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val v:View=inflater.inflate(R.layout.add_category_dialog,container,false)

        _binding1= BottomSheetBinding.inflate(inflater,container,false)
        var i=3
        var spinner:Spinner=binding1.bsSpinner
        ettd=binding1.etTaskDescription

        val spinnerAdapter=ArrayAdapter(v.context,R.layout.spinner_item,categoriess)
        spinnerAdapter.setDropDownViewResource(R.layout.dropdown_item)
        spinner.adapter=spinnerAdapter
        spinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                s=p0?.getItemAtPosition(p2).toString()
                if(s!="choose a category"){
                    binding1.addTask.isEnabled=true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }



        binding1.addTask.setOnClickListener {
                  var ettdtext:String=ettd.text.toString()
                  var newTaskElement: task = task(
                        1000,
                        "$s",
                        "$ettdtext",
                        true,
                        false,

                    )


                    tasks.add(0, newTaskElement)
                    adapterThis.notifyItemInserted(0)
                    val gson = GsonBuilder()
                    .setLenient()
                    .create()
                    val retrofitBuilder= Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(baseURL)
                    .build()
                    .create(apiInterface::class.java)
                    val postTask=retrofitBuilder.posttask("$s","$ettdtext",1,0,username)
                    postTask.enqueue(object : Callback<task?> {
                        override fun onResponse(call: Call<task?>, response: Response<task?>) {
                            print("on success"+response.body())
                        }

                        override fun onFailure(call: Call<task?>, t: Throwable) {
                            print("on failure "+t.message)
                        }
                    })
                    //val response=Request.Builder.post(baseURL+"post_task",{'category':'travel','description':'madrid','flag':1,'ischecked':0})
                    ettd.setText("")
                    dismiss()







        }

        return binding1.root
    }





    override fun onDestroyView() {
        super.onDestroyView()
            _binding1=null
    }
}
