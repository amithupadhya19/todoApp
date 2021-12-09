package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class signup : AppCompatActivity() {
    val baseURL="https://todoapp1900.herokuapp.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val signupButton=findViewById<Button>(R.id.signupButton)
        val user=findViewById<EditText>(R.id.usernameSignup)
        val pass=findViewById<EditText>(R.id.passwordSignup)

        signupButton.setOnClickListener {
            var username=user.text.toString()
            var password=pass.text.toString()
            if (password.length<8){
                Toast.makeText(this,"password must be 8 characters long",
                Toast.LENGTH_SHORT).show()
            }
            else {

                var t1 = signupThread(username,password)
                t1.start()

            }
        }
    }
    inner class signupThread(var username:String,var password:String):Thread(){
        override fun run() {

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofitBuilder= Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseURL)
                .build()
                .create(apiInterface::class.java)
            var url1:String= baseURL+"is_user_present?username="+username
            val retrofitData=retrofitBuilder.getUSERS(url1)
            retrofitData.enqueue(object : Callback<String?> {
                override fun onResponse(call: Call<String?>, response: Response<String?>) {
                    var value=response.body().toString()
                    if(value=="true"){
                        Toast.makeText(this@signup,"username is already taken",
                            Toast.LENGTH_SHORT).show()

                    }
                    else{
                        val gsonSignup = GsonBuilder()
                            .setLenient()
                            .create()
                        val retrofitBuilder= Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create(gsonSignup))
                            .baseUrl(baseURL)
                            .build()
                            .create(apiInterface::class.java)
                        val postUser=retrofitBuilder.postuser(username, password)
                        postUser.enqueue(object : Callback<user?> {
                            override fun onResponse(call: Call<user?>, response: Response<user?>) {
                                Toast.makeText(this@signup,"added new user "+username,
                                Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<user?>, t: Throwable) {
                                Toast.makeText(this@signup,"couldn't add user",Toast.LENGTH_SHORT).
                                        show()
                            }
                        })
                        val loginIntent= Intent(this@signup,login::class.java)
                        startActivity(loginIntent)
                    }
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    print("amith failure"+ t.message)
                }
            })
        }
    }
}