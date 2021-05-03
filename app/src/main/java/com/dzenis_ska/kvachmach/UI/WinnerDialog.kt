package com.dzenis_ska.kvachmach.UI

import android.app.Activity
import android.app.AlertDialog
import android.media.MediaPlayer
import android.widget.Toast
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.databinding.WinnerDiallogBinding
import kotlinx.coroutines.*


object WinnerDialog {

    fun createWinnerDialog(act: Activity, winner: String) {
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = WinnerDiallogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()



        CoroutineScope(Dispatchers.Main).launch {
            """
                    $winner
                    The best brain shaker!!!
                """.trimIndent().also { rootDialogElement.tvWinner.text = it }

            val player = MediaPlayer.create(act, R.raw.vals)
            player.isLooping = false
            startPlayer(player)
            dialog.dismiss()
            Toast.makeText(act, act.resources.getText(R.string.discharge_progress), Toast.LENGTH_LONG).show()
        }


    }

    suspend fun startPlayer(player: MediaPlayer) = withContext(Dispatchers.IO) {
        player.start()
        delay(11000)
    }
}