package com.dzenis_ska.kvachmach

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dzenis_ska.kvachmach.UI.ProgessFragment
import com.dzenis_ska.kvachmach.UI.ProgressFragmentAdapter

class ItemTouchMoveCallback(val adapter: ProgressFragmentAdapter, val fragment: ProgessFragment) : ItemTouchHelper.Callback(){
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlag, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)
            fragment.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }

    interface ItemTouchAdapter{
        fun onMove(startPos: Int, targetPos: Int)
    }
}