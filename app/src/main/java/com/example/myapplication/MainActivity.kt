package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import com.google.gson.Gson





class MainActivity : AppCompatActivity() ,CategoryAdapter.OnItemClickListener {





     /*var tasks = arrayListOf<task>(
        task(
            "1",
            "exercise",
            "bench press",
            true,
            false,

        ),
        task(
            "2",
            "shopping",
            "groceries",
            true,
            false,

        ),
        task(
            "3",
            "studies",
            "do homework",
            true,
            false,

        ),
        task(
            "4",
            "random",
            "have fun",
            true,
            false,

        ),
    )*/

    /*var dummyList = arrayListOf<task>(
        task(
            "1",
            "exercise",
            "bench press",
            true,
            false,
            false
        ),
        task(
            "2",
            "shopping",
            "groceries",
            true,
            false,
            false
        ),
        task(
            "3",
            "studies",
            "do homework",
            true,
            false,
            false
        ),
        task(
            "4",
            "random",
            "have fun",
            true,
            false,
            false
        ),
    )*/
    //fun gettasks(){
        //val retrofitBuilder:Retrofit.Builder
    //}
    var tasks= arrayListOf<task>()
   val myadapter=Adapter(tasks)
    var categories= arrayListOf<category>()
    var spinnerListCategories= arrayListOf<String>("choose a category")
    val baseURL="https://todoapp1900.herokuapp.com/"
    val adapter2 = CategoryAdapter(categories, myadapter, tasks, this)


    override fun onCreate(savedInstanceState: Bundle?) {



        val USER=intent.getStringExtra("user name")
        getTask(USER!!)
        getCategory(USER!!)
        val drawerLayout: DrawerLayout
        var s1: String = ""


        var newTasks = ArrayList<task>()
        super.onCreate(savedInstanceState)
        var binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //recycler view
        var recyclerview: RecyclerView = findViewById(R.id.recycler_view)
        recyclerview.adapter = myadapter
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        //second recyclerview

        var recyclerView2: RecyclerView = findViewById(R.id.category_view)
        recyclerView2.adapter = adapter2
        recyclerView2.layoutManager = LinearLayoutManager(this)
        recyclerView2.setHasFixedSize(true)
        drawerLayout = findViewById(R.id.drawer_layout)


        val button: ImageView = findViewById(R.id.menu)
        button.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        val btn_add_category = findViewById<Button>(R.id.btn_add_category)
        btn_add_category.setOnClickListener {
            var dialog = AddCategoryDialogFragment(spinnerListCategories, adapter2, categories,this,USER)
            dialog.show(supportFragmentManager, "AddCategoryDialog")
        }


        //bottomsheet initialisation
        val bottomSheetFragment = BottomSheetFragment(tasks, myadapter, spinnerListCategories, s1,USER)

        //addicon click function
        fun clicked() {
            //val checkBox: CheckBox = findViewById(R.id.check_box)



            bottomSheetFragment.show(supportFragmentManager, "bottomSheetDialog")
            //login test code
            //val t1=loginthread()
            //t1.start()

        }

        val addIcon: ImageView = findViewById(R.id.add_icon)
        addIcon.setOnClickListener { clicked() }
        //no status bar or fullscreen
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //all button filter
        val allButton=findViewById<CardView>(R.id.all)
        allButton.setOnClickListener {
            myadapter.filter(tasks, true)
            val text: TextView = findViewById(R.id.toolbartext)
            text.text="all"
            drawerLayout.closeDrawer(GravityCompat.START)
            val addIconhere: ImageView = findViewById(R.id.add_icon)
            addIconhere.isEnabled=true
        }
        //logout
        val sharedPreferences:SharedPreferences=getSharedPreferences("keep_me_logged_in",
            MODE_PRIVATE)
        val logout:ImageView=findViewById(R.id.logout)
        logout.setOnClickListener {
            val editor:SharedPreferences.Editor=sharedPreferences.edit()
            editor.clear()
            editor.commit()
            finish()
            val t2=logoutthread()
            t2.start()
        }


    }

    private fun getCategory(username:String) {
        var user=username
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofitBuilder=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseURL)
            .build()
            .create(apiInterface::class.java)
        val url1=baseURL+"get_categories?username="+user
        val retrofitData=retrofitBuilder.getCATEGORIES(url1)
        retrofitData.enqueue(object : Callback<ArrayList<category>?> {
            override fun onResponse(
                call: Call<ArrayList<category>?>,
                response: Response<ArrayList<category>?>
            ) {
                val responseBody=response.body()!!
                for(individual_category in responseBody){
                    val CategoryName=individual_category.name.toString()
                    if (CategoryName=="completed"){
                        continue
                    }
                    spinnerListCategories.add(CategoryName.toString())
                    val newCategory:category=category(CategoryName)
                    categories.add(newCategory)

                }
                adapter2.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ArrayList<category>?>, t: Throwable) {
                Log.d("MainActivity","onFailure"+t.message)
            }
        })
    }


    private fun getTask(username:String) {

        var user=username
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofitBuilder=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseURL)
            .build()
            .create(apiInterface::class.java)
        val url1=baseURL+"get_tasks?username="+user
        val retrofitData=retrofitBuilder.getTASK(url1)
        retrofitData.enqueue(object : Callback<ArrayList<task>?> {
            override fun onResponse(
                call: Call<ArrayList<task>?>,
                response: Response<ArrayList<task>?>
            ) {
                var responseBody=response.body()!!
                for (eachtask in responseBody){
                    val id = eachtask.id
                    val category=eachtask.category
                    val description=eachtask.description
                    val flag=eachtask.flag
                    val ischecked=eachtask.ischecked
                    val newtask=task(id, category, description, flag, ischecked)
                    tasks.add(newtask)
                }
                myadapter.notifyDataSetChanged()


            }

            override fun onFailure(call: Call<ArrayList<task>?>, t: Throwable) {
                Log.d("MainActivity","onFailure"+t.message)
            }
        })
    }

    //drawer category filter
    override fun onItemClick(position: Int) {

        val layout: DrawerLayout = findViewById(R.id.drawer_layout)
        var s: String = ""
        var newTask: ArrayList<task>
        val addIconhere: ImageView = findViewById(R.id.add_icon)
        newTask = arrayListOf()
        layout.closeDrawer(GravityCompat.START)
        val text: TextView = findViewById(R.id.toolbartext)
        text.text=categories[position].name
        s = categories[position].name
        text.text = s
        addIconhere.isEnabled = false
        for (x: task in tasks) {
            if (x.category.toLowerCase().toString() == s) {
                newTask.add(x)
            }
        }
        myadapter.filter(newTask, false)





    }
    inner class logoutthread:Thread(){
        override fun run() {
            val intent=Intent(this@MainActivity,login::class.java)
            //intent.putExtra("INPUT STRING","LOGGED IN")
            startActivity(intent)
        }
    }

}









