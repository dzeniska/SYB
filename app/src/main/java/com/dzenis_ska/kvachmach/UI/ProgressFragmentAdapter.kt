package com.dzenis_ska.kvachmach.UI

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dzenis_ska.kvachmach.Constants
import com.dzenis_ska.kvachmach.GamerProgressClass
import com.dzenis_ska.kvachmach.R

class ProgressFragmentAdapter(val list: MutableList<GamerProgressClass>, val fragment: ProgessFragment): RecyclerView.Adapter<ProgressFragmentAdapter.ViewHolder>() {
    var const = 0
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.tvName)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)
        val tvPercent = itemView.findViewById<TextView>(R.id.tvPercent)
        var shake = itemView.findViewById<ImageView>(R.id.imageViewShake)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressFragmentAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        val holder = ViewHolder(itemView)

        holder.shake.setOnClickListener(){
            fragment.setFav(holder.adapterPosition)
        }

//        itemView.setOnClickListener(){
//
//            holder.shake.setImageResource(R.drawable.ic_shake_out)
//        }
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProgressFragmentAdapter.ViewHolder, position: Int) {
        holder.name.text = list[position].name
        val fav = list[position].fav
        if (const == Constants.LIST_GAMERS){
            holder.shake.visibility = View.GONE
        }
        if(fav == 1){
            holder.shake.setImageResource(R.drawable.ic_shake_in)
        }else{
            holder.shake.setImageResource(R.drawable.ic_shake_out)
        }
        holder.progressBar.progress = list[position].progress
        holder.tvPercent.text = list[position].progress.toString()



    }
    fun updateAdapter(newList: MutableList<GamerProgressClass>, con: Int){
        list.clear()
        list.addAll(newList)
        const = con
        notifyDataSetChanged()
    }
}