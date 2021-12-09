package com.example.myapplication

import retrofit2.Call
import retrofit2.http.*

interface apiInterface {
    @GET
    fun getTASK(
        @Url url1:String
    ):Call<ArrayList<task>>
    @GET
    fun getCATEGORIES(
        @Url url1:String
    ):Call<ArrayList<category>>
    @GET
    fun getUSERS(
        @Url url1:String
    ):Call<String>
    @GET("get_tasks")
    fun gettask(): Call<ArrayList<task>>
    @GET("get_categories")
    fun getcategory():Call<ArrayList<category>>
    @FormUrlEncoded
    @POST("post_task")
    fun posttask(
        @Field("category") category:String,
        @Field("description") descripton:String,
        @Field("flag") flag:Int,
        @Field("ischecked") ischecked:Int,
        @Field("username")username:String
        ):Call<task>
    @FormUrlEncoded
    @POST("post_category")
    fun postcategory(
        @Field("name")category:String,
        @Field("username")username:String
    ):Call<category>
    @FormUrlEncoded
    @POST("update_task")
    fun updatetask(
        @Field("id")id:String
    ):Call<task>
    @FormUrlEncoded
    @POST("post_user")
    fun postuser(
        @Field("username") username:String,
        @Field("password") password:String,

    ):Call<user>
}