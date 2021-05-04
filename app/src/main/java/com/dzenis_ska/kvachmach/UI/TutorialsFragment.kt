package com.dzenis_ska.kvachmach.UI

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.dzenis_ska.kvachmach.GamerProgressClass
import com.dzenis_ska.kvachmach.MainActivity
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.databinding.FragmentTutorialsBinding
import com.dzenis_ska.kvachmach.databinding.WinnerDiallogBinding
import kotlinx.coroutines.*

class TutorialsFragment : Fragment() {
    lateinit var rootElement: FragmentTutorialsBinding
    private var job: Job? = null
    var bool: Boolean = true
    lateinit var player: MediaPlayer
    lateinit var viewModel: GameViewModel
    lateinit var player2: MediaPlayer
    private val changedGamers = mutableListOf<GamerProgressClass>()
    var quantityGamers = 0
    var numToast = 0

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


    @SuppressLint("SetTextI18n", "ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.title = resources.getString(R.string.app_name)
        (activity as AppCompatActivity).supportActionBar?.title = viewModel.title

//        if(changedGamers.size == 0){
//            job = CoroutineScope(Dispatchers.Main).launch {
//                viewModel.getAllNames()
//            }
//        }

        viewModel.liveNewName.observe(viewLifecycleOwner, Observer {
            Log.d("!!!", "TF${it.size}")
            changedGamers.clear()
            for (n in 0 until it.size) {
                if (it[n].fav == 1) changedGamers.add(it[n])
            }
            if (changedGamers.size > 1) {
                rootElement.tvQ.text = """ Первым(ой) отвечает:
                | ${changedGamers[0].name}""".trimMargin()
                Log.d("!!!", "TF${changedGamers.size}")
            } else {
                rootElement.tvQ.text = """Нет времени объяснять, жми 
                    |старт!""".trimMargin()
            }
        })

        init()

        rootElement.flBtnAddGamers.setOnClickListener() {
            navController.navigate(R.id.progessFragment)
        }

        rootElement.button.setOnClickListener {

            if (rootElement.button.text == resources.getString(R.string.save)) {
                if (rootElement.edQ.text.isNotEmpty()) {
                    rootElement.tvQ.setTextColor(resources.getColor(R.color.white))
                    rootElement.button.text = resources.getString(R.string.start)
                    rootElement.edQ.visibility = View.GONE
                    if (changedGamers.size > 1) {

//                        if(changedGamers[quantityGamers].progress >= 25){
//diallogWinner.createWinnerDialog(activity as MainActivity, "jopa")
//                        }

                        updateProgress(changedGamers[quantityGamers], rootElement.edQ.text.toString().toInt())



                        if (quantityGamers < changedGamers.size.minus(1)) {
                            rootElement.tvQ.text = """Следующим отвечает:
                                |${changedGamers[quantityGamers.plus(1)].name}""".trimMargin()
                            quantityGamers++
                        } else {
                            rootElement.tvQ.text = """Следующим отвечает:
                                |${changedGamers[0].name}""".trimMargin()
                            quantityGamers = 0
                        }
                    }
                    rootElement.edQ.text.clear()
                } else {
                    Toast.makeText(activity as MainActivity, "Введите количество правильных ответов игрока!", Toast.LENGTH_LONG).show()
                }
            } else {

                player.start()
                if (bool) {
                    bool = false

                    rootElement.tvQ.text = viewModel.getQuestion()

                    rootElement.button.visibility = View.GONE
                    job = CoroutineScope(Dispatchers.Main).launch {
                        rootElement.tvCounter.visibility = View.VISIBLE
                        for (j in 9 downTo 0) {
                            rootElement.tvCounter.text = j.toString()
                            count()
                        }
                        bool = true
                        rootElement.tvCounter.visibility = View.GONE
                        rootElement.button.visibility = View.VISIBLE
                        if (changedGamers.size > 1) {
                            rootElement.tvQ.setTextColor(resources.getColor(R.color.game_background_color))
                            rootElement.button.text = resources.getString(R.string.save)
                            rootElement.edQ.visibility = View.VISIBLE
                        } else {
                            rootElement.button.text = resources.getString(R.string.start)
                            rootElement.tvQ.setTextColor(resources.getColor(R.color.white))
                            rootElement.tvQ.text = "Хуйня! Давай ещё разок!"
                            if(numToast == 0){
                                val toast = Toast.makeText(activity as MainActivity, "Для записи результата необходимо 2 и более участника! \n Зайдите в пункт меню: \"Игроки и результаты\"!",
                                        Toast.LENGTH_LONG)
                                toast.setGravity(Gravity.CENTER, 0, 0)
                                val toastContainer: LinearLayout = toast.view as LinearLayout
                                toastContainer.setBackgroundColor(Color.TRANSPARENT)
                                toast.show()
                                numToast++
                            }
                         }

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
        val numProgress = numAnsvers/*.minus(numQuestion)*/

        val gpc = GamerProgressClass(id, fav, name, numQuestion, numAnsvers, numProgress)

        changedGamers.removeAt(quantityGamers)

        changedGamers.add(quantityGamers, gpc)




        if (numProgress >= 25)
        {

            WinnerDialog.createWinnerDialog(activity as MainActivity, name)

            Log.d("!!!win25", numProgress.toString())
        }
        Log.d("!!!win", numProgress.toString())


        viewModel.sendProgress(id, numQuestion, numAnsvers, numProgress * 4)

    }


    private fun init() {
        navController = findNavController()
        player = MediaPlayer.create(activity as MainActivity, R.raw.fart_2)
        player2 = MediaPlayer.create(activity as MainActivity, R.raw.fart_3)
        player.isLooping = false
        player2.isLooping = false
        if (viewModel.array.size == 0) viewModel.array.addAll(resources.getStringArray(R.array.question))

    }

    private suspend fun count() = withContext(Dispatchers.IO) {
        delay(1100)
    }


}