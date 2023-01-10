package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter (val itemList: ArrayList<ViewData>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val uid: TextView = view.findViewById(R.id.com_lv_uid)
        val title: TextView = view.findViewById(R.id.com_lv_title)
        val time: TextView = view.findViewById(R.id.com_lv_time)
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item_list_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.uid.text = itemList[position].uid
        holder.title.text = itemList[position].title
        holder.time.text = itemList[position].time
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}