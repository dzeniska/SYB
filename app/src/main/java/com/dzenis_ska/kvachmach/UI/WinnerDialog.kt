package com.dzenis_ska.kvachmach.UI

import android.app.Activity
import android.app.AlertDialog
import android.media.MediaPlayer
import android.view.View
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.databinding.WinnerDiallogBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.*


object WinnerDialog {

//    var interAd: InterstitialAd? = null
    var fragment: TutorialsFragment? = null

    fun createWinnerDialog(act: Activity, winner: String, tutorialsFragment: TutorialsFragment) {
        fragment = tutorialsFragment
        val builder = AlertDialog.Builder(act)
        val rootDialogElement = WinnerDiallogBinding.inflate(act.layoutInflater)
        val view = rootDialogElement.root
        builder.setView(view)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()


        CoroutineScope(Dispatchers.Main).launch {
            """
                    $winner:
                    The best brain shaker(s)!!!
                    
                """.trimIndent().also { rootDialogElement.tvWinner.text = it }

            val player = MediaPlayer.create(act, R.raw.vals)
            player.isLooping = false
            //загрузка рекламы
//            loadInterAd(act)
            startPlayer(player)
            //показ рекламы
//            showInterAd(dialog, act)
            tutorialsFragment.showInterAd()
            dialog.dismiss()
            fragment?.rootElement?.tvDownProgress?.visibility = View.VISIBLE
//            Toast.makeText(act, act.resources.getText(R.string.discharge_progress), Toast.LENGTH_LONG).show()
        }
    }

    suspend fun startPlayer(player: MediaPlayer) = withContext(Dispatchers.IO) {
        player.start()
        delay(11000)
    }

//    private fun loadInterAd(act: Activity) {
//        val adRequest = AdRequest.Builder().build()
//        InterstitialAd.load(
//                act,
//                act.resources.getString(R.string.ad_inter_id),
//                adRequest,
//                object : InterstitialAdLoadCallback(){
//                    override fun onAdLoaded(ad: InterstitialAd) {
//                        interAd = ad
//                    }
//                }
//        )
//    }
//
//    fun showInterAd(dialog: AlertDialog, act: Activity) {
//        if(interAd != null){
//            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
//                override fun onAdDismissedFullScreenContent() {
//                    dialog.dismiss()
//                }
//                override fun onAdFailedToShowFullScreenContent(ad: AdError) {
//                    dialog.dismiss()
//                }
//            }
//            interAd?.show(act)
//        }else{
//            dialog.dismiss()
//        }
//    }
}