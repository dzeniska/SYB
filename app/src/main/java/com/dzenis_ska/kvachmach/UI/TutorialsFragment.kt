package com.dzenis_ska.kvachmach.UI

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.dzenis_ska.kvachmach.GamerProgressClass
import com.dzenis_ska.kvachmach.LocalModel.LocalModel
import com.dzenis_ska.kvachmach.MainActivity
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.ViewModel.GameViewModelFactory
import com.dzenis_ska.kvachmach.databinding.FragmentTutorialsBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.*

class TutorialsFragment : Fragment() {
    var rootElement: FragmentTutorialsBinding? = null
    private var job: Job? = null
    var bool: Boolean = true
    lateinit var player: MediaPlayer
//    lateinit var viewModel: GameViewModel
    private val viewModel: GameViewModel by activityViewModels{
        GameViewModelFactory(LocalModel(context as MainActivity))
    }
    lateinit var player2: MediaPlayer
    private val changedGamers = mutableListOf<GamerProgressClass>()
    var quantityGamers = 0
    var numToast = 0
    var interAd: InterstitialAd? = null

    lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootElement = FragmentTutorialsBinding.inflate(inflater)
        val view = rootElement!!.root

//        val localModel = LocalModel(activity as MainActivity)
//        val factory = GameViewModelFactory(localModel)
//        viewModel = ViewModelProvider(activity as MainActivity, factory).get(GameViewModel::class.java)

        return view
    }

    @SuppressLint("SetTextI18n", "ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootElement!!.edQ.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                saveCount()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }

        viewModel.title = resources.getString(R.string.app_name)
        (activity as AppCompatActivity).supportActionBar?.title = viewModel.title

        viewModel.liveNewName.observe(viewLifecycleOwner, Observer {
            Log.d("!!!", "TF${it.size}")
            changedGamers.clear()
            for (n in 0 until it.size) {
                if (it[n].fav == 1) changedGamers.add(it[n])
            }

            if (changedGamers.size > 1) {
                rootElement!!.tvQ.text = """ Первым(ой) отвечает:
                | ${changedGamers[0].name}""".trimMargin()

                viewModel.quantityGamers = changedGamers.size

//                Log.d("!!!size", "TF${viewModel.countGamers}")
            } else {
                rootElement!!.tvQ.text = """Времени не существует, 
                    |впринципе,
                    |жми START!""".trimMargin()
            }
        })

        init()
        rootElement!!.apply {
            button.setOnClickListener {

                if (rootElement!!.button.text == resources.getString(R.string.save)) {
                    saveCount()
                } else {
                    if(tvDownProgress.visibility == View.VISIBLE) tvDownProgress.visibility = View.GONE
                    player.start()
                    if (bool) {
                        bool = false
                        tvQ.text = viewModel.getQuestion()
                        button.visibility = View.GONE
                        job = CoroutineScope(Dispatchers.Main).launch {
                            tvCounter.visibility = View.VISIBLE
                            for (j in 9 downTo 0) {
                                tvCounter.text = j.toString()
                                count()
                            }
                            bool = true
                            tvCounter.visibility = View.GONE
                            button.visibility = View.VISIBLE
                            if (changedGamers.size > 1) {
                                tvQ.setTextColor(ContextCompat.getColor(activity as MainActivity, R.color.game_background_color))
//                            rootElement.tvQ.setTextColor(resources.getColor(R.color.game_background_color))
                                button.text = resources.getString(R.string.save)
                                edQ.visibility = View.VISIBLE
                                showSoftKeyboard(edQ)
                            } else {
                                button.text = resources.getString(R.string.start)
                                tvQ.setTextColor(ContextCompat.getColor(activity as MainActivity, R.color.white))
//                            rootElement.tvQ.setTextColor(resources.getColor(R.color.white))
                                tvQ.text = "Маладца! Давай ещё разок!"
                                if (numToast == 0) {
                                    val toast = Toast.makeText(activity as MainActivity, "Для записи результата необходимо 2 и более участника! \n Зайдите в пункт меню: \"Игроки и результаты\"!",
                                        Toast.LENGTH_LONG)
                                    toast.setGravity(Gravity.CENTER, 0, 0)
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                                        toast.show()
                                    } else {
                                        val toastContainer: LinearLayout = toast.view as LinearLayout
                                        toastContainer.setBackgroundColor(Color.TRANSPARENT)
                                        toast.show()
                                    }
                                    numToast++
                                }
                            }
                            player2.start()
                        }
                    }
                }
            }
        }

    }

    private fun saveCount() {
        rootElement!!.apply{
            if (edQ.text.isNotEmpty()) {
                tvQ.setTextColor(ContextCompat.getColor(activity as MainActivity, R.color.white))
//                    rootElement.tvQ.requireCotext().getCompatColor(R.color.white)
                button.text = resources.getString(R.string.start)
                edQ.visibility = View.GONE
                if (changedGamers.size > 1) {

                    updateProgress(changedGamers[quantityGamers], edQ.text.toString().toInt())

                    if (quantityGamers < changedGamers.size.minus(1)) {
                        tvQ.text = """Следующим отвечает:
                                |${changedGamers[quantityGamers.plus(1)].name}""".trimMargin()
                        quantityGamers++
                    } else {
                        tvQ.text = """Следующим отвечает:
                                |${changedGamers[0].name}""".trimMargin()
                        quantityGamers = 0
                    }
                }
                edQ.text.clear()
            } else {
                Toast.makeText(activity as MainActivity, "Введите количество правильных ответов игрока!", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun updateProgress(gamerProgressClass: GamerProgressClass, ansv: Int) {
        val id = gamerProgressClass.id
        val name = gamerProgressClass.name
        val fav = gamerProgressClass.fav
        val numQuestion = gamerProgressClass.questions.plus(1)
        val numAnsvers = gamerProgressClass.answers.plus(ansv)
        val numProgress = numAnsvers/*.minus(numQuestion)*/

        val gpc = GamerProgressClass(id, fav, name, numQuestion, numAnsvers, numProgress)

        changedGamers.removeAt(quantityGamers)

        changedGamers.add(quantityGamers, gpc)

        viewModel.sendProgress(id, numQuestion, numAnsvers, numProgress * 4)

        if(viewModel.everyLap()){

            val list = mutableListOf<Int>()
            val listName = mutableListOf<String>()
            var lieder = ""
            for (n in 0 until changedGamers.size) {
                list.add(changedGamers[n].progress)
            }
            val maximum = list.maxOrNull()
            for (n in 0 until changedGamers.size) {
                if(changedGamers[n].progress == maximum){
                    listName.add(changedGamers[n].name)
                    lieder = lieder.plus(changedGamers[n].name).plus(", ")
                }
            }
            lieder = lieder.substringBeforeLast(',')
            if (maximum != null && maximum >= 25) {

                WinnerDialog.createWinnerDialog(activity as MainActivity, lieder, this)

//                rootElement.tvDownProgress.visibility = View.VISIBLE
            }else{
                if(listName.size > 1){
                    Toast.makeText(activity as MainActivity, "Лидируют: ${lieder}", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(activity as MainActivity, "Лидирует: ${lieder}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun init() {
        loadInterAd()
        navController = findNavController()
        player = MediaPlayer.create(activity as MainActivity, R.raw.fart_2)
        player2 = MediaPlayer.create(activity as MainActivity, R.raw.fart_3)
        player.isLooping = false
        player2.isLooping = false
        if (viewModel.array.size == 0) viewModel.array.addAll(resources.getStringArray(R.array.question))
    }

    private suspend fun count() = withContext(Dispatchers.IO) {
        delay(1000)
    }

    private fun loadInterAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context as MainActivity,
            resources.getString(R.string.ad_inter_id),
            adRequest,
            object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interAd = null
                }
                override fun onAdLoaded(ad: InterstitialAd) {
                    interAd = ad
                }
            }
        )
    }

    fun showInterAd() {
        if(interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    interAd = null
                    loadInterAd()
                }
                override fun onAdFailedToShowFullScreenContent(ad: AdError) {
                    interAd = null
                    loadInterAd()
                }
                override fun onAdShowedFullScreenContent() {
                    interAd = null
                }
            }
            interAd?.show(context as MainActivity)
        }else{
            interAd = null
            loadInterAd()
        }
    }
    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val inputMethodManager: InputMethodManager = getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}