 package com.dzenis_ska.kvachmach.UI

import android.app.Activity
import android.app.AlertDialog
import androidx.core.view.GravityCompat
import com.dzenis_ska.kvachmach.MainActivity
import com.dzenis_ska.kvachmach.databinding.DiallogInstrBinding


 class DialogInstr(val activity: MainActivity) {
    fun createInstrDialog(act: Activity): AlertDialog{
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = DiallogInstrBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)
        val dialog = builder.create()
//        dialog.setCancelable(false)
        dialog.show()

        rootDialogElement.btnUnderstand.setOnClickListener(){
            dialog.dismiss()
        }

        return dialog
    }
}