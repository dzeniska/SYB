package com.dzenis_ska.kvachmach.UI

import android.annotation.SuppressLint
import android.util.Log
import androidx.navigation.NavController


object InitBackStack {

    @SuppressLint("RestrictedApi")
    fun initBackStack(navController: NavController) {
        val fList = navController.backQueue
        var count = 0
        fList.forEach {
            Log.d("!!!navController", "${it.destination.label}")
//            if(it.destination.id == fragmentId) count++
//            if(count > 1) navController.popBackStack(fragmentId, true)
        }
    }

    fun deleteBackStack(navController: NavController) {
        val fList = navController.backQueue

        fList.forEach {

        }
    }
}