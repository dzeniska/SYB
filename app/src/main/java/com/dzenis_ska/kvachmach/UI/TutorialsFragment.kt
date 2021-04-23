package com.dzenis_ska.kvachmach.UI

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dzenis_ska.kvachmach.Constants
import com.dzenis_ska.kvachmach.GamerProgressClass
import com.dzenis_ska.kvachmach.MainActivity
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.databinding.FragmentTutorialsBinding
import kotlinx.coroutines.*

class TutorialsFragment : Fragment() {
    lateinit var rootElement: FragmentTutorialsBinding
    private var job: Job? = null
    var bool:Boolean = true
    lateinit var player: MediaPlayer
    lateinit var viewModel: GameViewModel
    lateinit var player2: MediaPlayer
    private val changedGamers = mutableListOf<GamerProgressClass>()
    var quantityGamers = 0

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //in here you can do logic when backPress is clicked
                navController.popBackStack(R.id.progessFragment, true)
                navController.popBackStack(R.id.tutorialsFragment, true)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootElement = FragmentTutorialsBinding.inflate(inflater)
        val view = rootElement.root
        viewModel = ViewModelProvider(activity as MainActivity).get(GameViewModel::class.java)
        return view
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveNewName.observe(viewLifecycleOwner, Observer{
            Log.d("!!!", "TF${it.size}")
            changedGamers.clear()
            for(n in 0 until it.size){
                if(it[n].fav == 1) changedGamers.add(it[n])
            }

            rootElement.tvQ.text = """ Первымм отвечать будет:
                | ${changedGamers[0].name}""".trimMargin()
            Log.d("!!!", "TF${changedGamers.size}")
        })
        viewModel.liveNewName2.observe(viewLifecycleOwner, Observer{
            changedGamers.clear()
            for(n in 0 until it.size){
                if(it[n].fav == 1) changedGamers.add(it[n])
            }
        })
        init()


        val array: Array<String> = resources.getStringArray(R.array.question)
        val quantity = array.size

        rootElement.flBtnAddGamers.setOnClickListener(){
            navController.navigate(R.id.progessFragment)
        }

        rootElement.button.setOnClickListener {

            if (rootElement.button.text == resources.getString(R.string.save)){
                if(rootElement.edQ.text.isNotEmpty()){
                    rootElement.button.text = resources.getString(R.string.start)
                    rootElement.edQ.visibility = View.GONE
                    if(changedGamers.size > 1){
//                        Log.d("!!!answ", "${changedGamers[quantityGamers]}")
//                        Log.d("!!!answ", "${quantityGamers}")

                        updateProgress(changedGamers[quantityGamers], rootElement.edQ.text.toString().toInt())

                        if(quantityGamers < changedGamers.size.minus(1)){
                            rootElement.tvQ.text = """Следующим отвечает:
                                |${changedGamers[quantityGamers.plus(1)].name}""".trimMargin()
                            quantityGamers++
                        }else{
                            rootElement.tvQ.text = """Следующим отвечает:
                                |${changedGamers[0].name}""".trimMargin()
                            quantityGamers = 0
                        }

                    }
                    rootElement.edQ.text.clear()
                }else {
                    Toast.makeText(activity as MainActivity, "Введите количество правильных ответов игрока!", Toast.LENGTH_LONG).show()
                }
            }else {
//                rootElement.flBtnAddGamers.visibility= View.GONE
                player.start()
                if (bool) {
                    bool = false
                    rootElement.tvQ.text = array[(0 until quantity).random()]
                    rootElement.button.visibility = View.GONE
                    job = CoroutineScope(Dispatchers.Main).launch {
                        for (j in 1 downTo -1) {
                            count()
                            rootElement.tvCounter.visibility = View.VISIBLE
                            rootElement.tvCounter.text = j.toString()
                        }
                        bool = true
                        rootElement.tvCounter.visibility = View.GONE
                        rootElement.button.visibility = View.VISIBLE
                        rootElement.button.text = resources.getString(R.string.save)
                        rootElement.edQ.visibility = View.VISIBLE
                        player2.start()
                    }

                }
            }

        }

    }


    private fun updateProgress(gamerProgressClass: GamerProgressClass, ansv: Int) {

        val id = gamerProgressClass.id
        val name = gamerProgressClass.name
        val fav = gamerProgressClass.fav
        val numQuestion = gamerProgressClass.questions.plus(1)
        val numAnsvers = gamerProgressClass.answers.plus(ansv)
        val numProgress = 100.minus(numQuestion*100/numAnsvers)
        val gpc = GamerProgressClass(id, fav, name, numQuestion, numAnsvers, numProgress)

        changedGamers.removeAt(quantityGamers)

        changedGamers.add(quantityGamers, gpc)
        viewModel.sendProgress(id, numQuestion, numAnsvers, numProgress)
    }


    private fun init() {
        navController = findNavController()
//        viewModel.getAllNames()
        player = MediaPlayer.create(activity as MainActivity, R.raw.fart_2)
        player2 = MediaPlayer.create(activity as MainActivity, R.raw.fart_3)
        player.isLooping = false
        player2.isLooping = false

    }

    private suspend fun count() = withContext(Dispatchers.IO) {
            delay(1200)
    }


}