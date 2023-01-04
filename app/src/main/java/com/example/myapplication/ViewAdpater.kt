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
        val name: TextView = view.findViewById(R.id.name)
        val previewText: TextView = view.findViewById(R.id.text_preview)
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.previewText.text = itemList[position].preview_text
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}