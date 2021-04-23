package com.dzenis_ska.kvachmach.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.dzenis_ska.kvachmach.MainActivity
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    lateinit var rootElement: FragmentGameBinding
    lateinit var anim: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootElement = FragmentGameBinding.inflate(inflater)
        val view = rootElement.root
        // Inflate the layout for this fragment
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("!!!", "!!!fragment")
        anim = AnimationUtils.loadAnimation(activity as MainActivity, R.anim.alpha)
        rootElement.tvRules.text = """
                                        |У тебя совсем нет времени объяснять.
                                        |Времени хватит только на то, чтобы перечислить несколько понятий на указанную тему.
                                        |Успеешь меньше чем за 10 секунд назвать четырёх композиторов?
                                        |Проверь себя в этой увлекательной викторине!""".trimMargin()
        rootElement.tvRules.startAnimation(anim)

    }

}