package com.example.myapplication

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(var categoryList:ArrayList<category>, adapter1:Adapter,
                       tasks:ArrayList<task>,

                        val listener:OnItemClickListener)
    :RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var adapter=adapter1
    var taskList=tasks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.drawer_category_layout,
        parent,false)

        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var currentItem=categoryList[position]
        holder.cat_name.text=currentItem.name

    }

    override fun getItemCount()=categoryList.size

    inner class CategoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        var cat_name:TextView=itemView.findViewById(R.id.drawer_category_tv)



        init{
            itemView.setOnClickListener(this)
            }

        override fun onClick(p0: View?) {
            val position:Int=absoluteAdapterPosition
            if(position!=RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    }
