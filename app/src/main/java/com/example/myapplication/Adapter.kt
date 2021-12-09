package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Adapter(var tasks: ArrayList<task>) : RecyclerView.Adapter<Adapter.ViewHolder >() {
    public var flag:Boolean=true
    var dummylist=ArrayList<task>()
    val baseURL="https://todoapp1900.herokuapp.com/"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card,
            parent, false)
        return ViewHolder(itemView)
    }
    fun filter(newList:ArrayList<task>,flagval:Boolean){
        flag=flagval
        dummylist=tasks
        tasks=newList
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = tasks[position]
        holder.categoryname.text=currentItem.category
        holder.taskdescription.text= currentItem.description
        holder.checkBox.isEnabled=currentItem.flag
        holder.checkBox.isChecked=currentItem.ischecked
       /* holder.checkBox.setOnClickListener {

                val task:task=tasks[position]
                dummylist.remove(task)
                val description:String=tasks[position].task_description.toString()
                var i=0
                val category:String="completed"
                val newCompletedTask:task=task("$i",
                    "$category","$description",false,true,true)
                dummylist.add(dummylist.size,newCompletedTask)

                tasks.removeAt(position)
                notifyItemRemoved(position)

                tasks.add(tasks.size, newCompletedTask)
                notifyItemInserted(tasks.size)






        }*/




    }



    override fun getItemCount()=tasks.size





    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var categoryname: TextView
        var taskdescription: TextView
        var checkBox: CheckBox
        var flaghere=flag



        init {

            categoryname = itemView.findViewById(R.id.category_name)
            taskdescription = itemView.findViewById(R.id.task_description)
            checkBox = itemView.findViewById(R.id.check_box)
            checkBox.setOnClickListener {

                val pos:Int=absoluteAdapterPosition
                val task:task=tasks[pos]
                dummylist.remove(task)
                val description:String=tasks[pos].description.toString()
                var i=tasks[pos].id
                val category:String="completed"
                //apicall
               val t1=updateThread(i)
                t1.start()

                val newCompletedTask:task=task(i,
                    "$category","$description",false,true)
                 dummylist.add(dummylist.size,newCompletedTask)

                    tasks.removeAt(pos)
                    notifyItemRemoved(pos)

                    tasks.add(tasks.size, newCompletedTask)
                    notifyItemInserted(tasks.size)

            }

        }


    }
    inner class updateThread(val id:Int): Thread() {
        override fun run() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofitBuilder= Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseURL)
                .build()
                .create(apiInterface::class.java)
            val updateTask=retrofitBuilder.updatetask("$id")
            updateTask.enqueue(object : Callback<task?> {
                override fun onResponse(call: Call<task?>, response: Response<task?>) {
                    print("on success"+response.body())
                }

                override fun onFailure(call: Call<task?>, t: Throwable) {
                    print("on failure "+t.message)
                }
            })

        }
    }


}

