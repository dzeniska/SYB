package com.dzenis_ska.kvachmach.UI

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class NameCallback(val fragment: ProgessFragment): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT ){

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        fragment.deleteName(position)
    }
}
