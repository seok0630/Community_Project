package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ViewAdpater (val itemList: ArrayList<ViewData>) :
    RecyclerView.Adapter<ViewAdpater.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val uid: TextView = view.findViewById(R.id.lv_uid)
        val title: TextView = view.findViewById(R.id.lv_title)
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.uid.text = itemList[position].uid
        holder.title.text = itemList[position].title
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}