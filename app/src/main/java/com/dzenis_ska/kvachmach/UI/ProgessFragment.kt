package com.dzenis_ska.kvachmach.UI

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzenis_ska.kvachmach.*
import com.dzenis_ska.kvachmach.LocalModel.LocalModel
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.ViewModel.GameViewModelFactory
import com.dzenis_ska.kvachmach.databinding.FragmentProgessBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.*
import java.util.*


class ProgessFragment : Fragment(), ItemTouchMoveCallback.ItemTouchAdapter {
    lateinit var rootElement: FragmentProgessBinding
    lateinit var viewModel: GameViewModel
    private val names = mutableListOf<GamerProgressClass>()
    val act = MainActivity()




    val adapter = ProgressFragmentAdapter(names, this)
    val dragCallback = ItemTouchMoveCallback(adapter, this)
    val touchHelper = ItemTouchHelper(dragCallback)
    lateinit var anim: Animation
    private var job: Job? = null


    var id: Int? = 0
    var pref: SharedPreferences? = null
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        pref = this.activity?.getSharedPreferences("FUCK", 0)
        id = pref?.getInt("counter", 1)!!
        Log.d("!!!", id.toString())
        rootElement.adView.resume()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootElement = FragmentProgessBinding.inflate(inflater)
        val view = rootElement.root

        val localModel = LocalModel(activity as MainActivity)
        val factory = GameViewModelFactory(localModel)
        viewModel = ViewModelProvider(activity as MainActivity, factory).get(GameViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.title = resources.getString(R.string.add_gamer)
                 (activity as AppCompatActivity).supportActionBar?.title = viewModel.title

        init()

        viewModel.liveNewName.observe(viewLifecycleOwner, Observer{

            job = CoroutineScope(Dispatchers.Main).launch {
//                count()
                if(viewModel.index == 0){
                    adapter.updateAdapter(it, Constants.CHANGE_GAMERS)
                }
            }

            if(it.size == 0){
                rootElement.floatingActionButton.startAnimation(anim)
            }else{
                rootElement.floatingActionButton.clearAnimation()
            }
        })

        rootElement.floatingActionButton.setOnClickListener(){
            if(rootElement.edEnterName.text.isNotEmpty()){
                val name = rootElement.edEnterName.text.toString()
                if (name.toLowerCase(Locale.ROOT) == "жопа"){
                    Toast.makeText(activity as MainActivity, "Да не \"Ж..па\" вводи, а имя своё, а жопа это Ты", Toast.LENGTH_SHORT).show()
                }else{
                    val newName = GamerProgressClass(id!!, 1, rootElement.edEnterName.text.toString(), 0,0, 0)
                    id = id?.plus(1)
                    viewModel.insertNewName(newName)
                }
            }else{
                Toast.makeText(activity as MainActivity, "Введи имя, Ж..па!", Toast.LENGTH_SHORT).show()
            }
            rootElement.edEnterName.text.clear()
        }


    }

    private suspend fun count() = withContext(Dispatchers.IO) {
        delay(30)
    }

    private fun init() {
        navController = findNavController()
        rootElement.recyclerViewGamers.adapter = adapter
        rootElement.recyclerViewGamers.layoutManager = LinearLayoutManager(activity)

        initAds()

        touchHelper.attachToRecyclerView(rootElement.recyclerViewGamers)
        anim = AnimationUtils.loadAnimation(activity as MainActivity, R.anim.alpha_fab)
    }
    override fun onMove(startPos: Int, targetPos: Int) {
        viewModel.replase(startPos, targetPos)
    }

    fun deleteName(id: Int){
//        Log.d("!!!", "id = $id")
        viewModel.deleteName(id)
    }

    override fun onPause() {
        super.onPause()
        saveData(id)
        viewModel.bool = false
        rootElement.adView.pause()
    }
    private fun saveData(i: Int?){
        val edit = pref?.edit()
        edit?.putInt("counter", i!!)
        edit?.apply()
    }

    fun setFav(adapterPosition: Int) {
        viewModel.isFav(adapterPosition)
    }
    fun clearProgress(adapterPosition: Int){
        viewModel.clearProgress(adapterPosition)
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.bool = false
    }

    override fun onDestroy() {
        super.onDestroy()
        rootElement.adView.destroy()
    }

    private fun initAds(){
        MobileAds.initialize(activity as MainActivity)
        val adRequest = AdRequest.Builder().build()
        rootElement.adView.loadAd(adRequest)
    }

}