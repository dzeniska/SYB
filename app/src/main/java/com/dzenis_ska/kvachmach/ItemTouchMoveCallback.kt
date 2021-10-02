package com.dzenis_ska.kvachmach

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView
import com.dzenis_ska.kvachmach.UI.ProgessFragment
import com.dzenis_ska.kvachmach.UI.ProgressFragmentAdapter

class ItemTouchMoveCallback(val adapter: ProgressFragmentAdapter, val fragment: ProgessFragment) :
    ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.START or ItemTouchHelper.END
    ) {

//    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        return makeMovementFlags(dragFlag, 0)
//    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)
        fragment.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.START) {
            fragment.deleteName(viewHolder.adapterPosition)
        } else if (direction == ItemTouchHelper.END) {
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

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
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

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.7f

//    override fun onChildDraw(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        //https://coderoad.ru/30820806/%D0%94%D0%BE%D0%B1%D0%B0%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-%D1%86%D0%B2%D0%B5%D1%82%D0%BD%D0%BE%D0%B3%D0%BE-%D1%84%D0%BE%D0%BD%D0%B0-%D1%81-%D1%82%D0%B5%D0%BA%D1%81%D1%82%D0%BE%D0%BC-%D0%B7%D0%BD%D0%B0%D1%87%D0%BA%D0%BE%D0%BC-%D0%BF%D0%BE%D0%B4-%D0%BF%D1%80%D0%BE%D0%BB%D0%B8%D1%81%D1%82%D0%B0%D0%BD%D0%BD%D0%BE%D0%B9-%D1%81%D1%82%D1%80%D0%BE%D0%BA%D0%BE%D0%B9-%D0%BF%D1%80%D0%B8
////            icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_delete_sweep)
//        val itemView: View = viewHolder.itemView
//
//        val p = Paint()
//
////        val icon: Bitmap = BitmapFactory.decodeResource(
////            recyclerView.context.resources, R.drawable.braintwo)
//
//        // Draw background
//        if(dX < 0){
//            p.color = Color.RED
//            c.drawRoundRect(
//                itemView.right.toFloat() + dX,
//                itemView.top.toFloat(),
//                itemView.right.toFloat(),
//                itemView.bottom.toFloat(),
//                180f,
//                180f, p)
//        }else{
//            p.color = Color.BLUE
//            c.drawRoundRect(
//                itemView.right.toFloat() - dX,
//                itemView.top.toFloat(),
//                itemView.right.toFloat(),
//                itemView.bottom.toFloat(),
//                180f,
//                180f, p)
//        }
//
//        //Draw icon
////        val iconMarginRight = (dX * -0.1f).coerceAtMost(70f).coerceAtLeast(0f)
////        c.drawBitmap(icon,
////            itemView.right.toFloat() - iconMarginRight - icon.width,
////            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
////            p)
//
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//    }

    interface ItemTouchAdapter {
        fun onMove(startPos: Int, targetPos: Int)
    }
}