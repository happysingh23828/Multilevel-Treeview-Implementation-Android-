package com.example.happy.filter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CustomAdapter(var context: Context, var idsList : MutableList<String>)  : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view  = LayoutInflater.from(context).inflate(R.layout.single_id_list,parent,false)
            return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  idsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userIdText.text = "User id = ${idsList[position]}"
    }

    inner  class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        var userIdText : TextView = itemView!!.findViewById(R.id.userid)
    }
}