package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewAdpater (val view: View, val itemList: ArrayList<ViewData>) :
    RecyclerView.Adapter<ViewAdpater.ViewHolder>() {

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val uid: TextView = view.findViewById(R.id.lv_uid)
        val title: TextView = view.findViewById(R.id.lv_title)
        val rootview: View = view.findViewById(R.id.lv_rootview)
    }

    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.uid.text = itemList[position].uid
        holder.title.text = itemList[position].title
        holder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}