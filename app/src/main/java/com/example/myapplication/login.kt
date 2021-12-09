package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class login : AppCompatActivity() {
    val baseURL = "https://todoapp1900.herokuapp.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //var MESSAGE:String?=intent.getStringExtra("INPUT STRING")
        //val tv:TextView=findViewById(R.id.logintext)
        //tv.setText(MESSAGE)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val LoginButton = findViewById<Button>(R.id.loginButton)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("keep_me_logged_in", MODE_PRIVATE)
        //check shared preference info
        val name = sharedPreferences.getString("usernameKEY", null)
        if(name!=null){
            //val usertext=username.text.toString()
            val t2=sharedpreferenceLoginThread(name)
            t2.start()
        }

        LoginButton.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            var user = username.text.toString()
            var pass = password.text.toString()
            editor.putString("usernameKEY", user)
            editor.putString("passwordKEY", pass)
            editor.apply()
            var t1 = loginThread(user, pass)
            t1.start()
            username.setText("")
            password.setText("")
        }
        val createAccount = findViewById<TextView>(R.id.createAccount)
        createAccount.setOnClickListener {
            val SignupIntent = Intent(this, signup::class.java)
            startActivity(SignupIntent)
        }
    }

    inner class loginThread(var username: String, var password: String) : Thread() {
        override fun run() {

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseURL)
                .build()
                .create(apiInterface::class.java)
            var url1: String = baseURL + "get_users?username=" + username + "&password=" + password
            val retrofitData = retrofitBuilder.getUSERS(url1)
            retrofitData.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    var value = response.body().toString()
                    if (value == "true") {
                        val loginIntent = Intent(this@login, MainActivity::class.java)
                        loginIntent.putExtra("user name", username)
                        startActivity(loginIntent)
                    } else {
                        Toast.makeText(
                            this@login, "please enter valid username and password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    print("amith failure" + t.message)
                }
            })
        }
    }

    inner class sharedpreferenceLoginThread(val username:String) : Thread() {
        override fun run() {
            val intent= Intent(this@login, MainActivity::class.java)
            intent.putExtra("user name",username)
            startActivity(intent)
        }
    }

}