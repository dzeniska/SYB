package com.dzenis_ska.kvachmach.UI

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.dzenis_ska.kvachmach.LocalModel.LocalModel
import com.dzenis_ska.kvachmach.MainActivity
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.ViewModel.GameViewModelFactory
import com.dzenis_ska.kvachmach.databinding.FragmentGameBinding

class GameFragment() : Fragment() {
    private var rootElement: FragmentGameBinding? = null
    lateinit var anim: Animation

    private val viewModel: GameViewModel by activityViewModels{
        GameViewModelFactory(LocalModel(context as MainActivity))
    }

    override fun onDestroy() {
        super.onDestroy()
        rootElement = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootElement = FragmentGameBinding.inflate(inflater)
        return rootElement?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//                (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.tutorial)
                (activity as AppCompatActivity).supportActionBar?.title = viewModel.title
        viewModel.title = resources.getString(R.string.tutorial)

        anim = AnimationUtils.loadAnimation(activity as MainActivity, R.anim.alpha)
        rootElement!!.tvRules.text = """
                                        |У тебя совсем нет времени объяснять.
                                        |Времени хватит только на то, чтобы перечислить несколько понятий на указанную тему.
                                        |Успеешь меньше чем за 10 секунд назвать четырёх композиторов?
                                        |Проверь себя в этой увлекательной викторине!
                                        |
                                        |                           by dzenis_ska""".trimMargin()
        rootElement!!.tvRules.startAnimation(anim)
        rootElement!!.imgTut.startAnimation(anim)

    }

}