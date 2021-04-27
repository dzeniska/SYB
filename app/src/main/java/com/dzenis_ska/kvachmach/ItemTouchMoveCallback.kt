package com.dzenis_ska.kvachmach

import android.annotation.SuppressLint
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView
import com.dzenis_ska.kvachmach.UI.ProgessFragment
import com.dzenis_ska.kvachmach.UI.ProgressFragmentAdapter

class ItemTouchMoveCallback(val adapter: ProgressFragmentAdapter, val fragment: ProgessFragment) :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END){

//    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        return makeMovementFlags(dragFlag, 0)
//    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)
            fragment.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if(direction == ItemTouchHelper.START){
            fragment.deleteName(viewHolder.adapterPosition)
        }else{
            fragment.clearProgress(viewHolder.adapterPosition)
        }


    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
//        val name = viewHolder?.itemView?.findViewById<TextView>(R.id.tvName)
//        val tvQuestions = viewHolder?.itemView?.findViewById<TextView>(R.id.tvQuestions)
//        val tvAnswers = viewHolder?.itemView?.findViewById<TextView>(R.id.tvAnswers)
        val constL1 = viewHolder?.itemView?.findViewById<ConstraintLayout>(R.id.constL1)
        val constL = viewHolder?.itemView?.findViewById<ConstraintLayout>(R.id.constL)
//        val statusBar = viewHolder?.itemView?.findViewById<ProgressBar>(R.id.progressBar)

            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                constL?.visibility = View.VISIBLE
                constL1?.visibility = View.GONE
//                    name?.visibility = View.GONE
//                    statusBar?.visibility = View.GONE
//                    tvAnswers?.visibility = View.GONE
//                    tvQuestions?.visibility = View.GONE
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//        val name = viewHolder.itemView.findViewById<TextView>(R.id.tvName)
//        val tvQuestions = viewHolder.itemView.findViewById<TextView>(R.id.tvQuestions)
//        val tvAnswers = viewHolder.itemView.findViewById<TextView>(R.id.tvAnswers)
        val constL1 = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.constL1)
        val constL = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.constL)
//        val statusBar = viewHolder.itemView.findViewById<ProgressBar>(R.id.progressBar)

        constL?.visibility = View.GONE
        constL1?.visibility = View.VISIBLE
//        statusBar?.visibility = View.VISIBLE
//        name?.visibility = View.VISIBLE
//        tvAnswers?.visibility = View.VISIBLE
//        tvQuestions?.visibility = View.VISIBLE
        super.clearView(recyclerView, viewHolder)
    }

    interface ItemTouchAdapter{
        fun onMove(startPos: Int, targetPos: Int)
    }
}