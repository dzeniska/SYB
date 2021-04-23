package com.dzenis_ska.kvachmach.UI

import android.content.SharedPreferences
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzenis_ska.kvachmach.Constants
import com.dzenis_ska.kvachmach.GamerProgressClass
import com.dzenis_ska.kvachmach.MainActivity
import com.dzenis_ska.kvachmach.R
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.databinding.FragmentProgessBinding
import com.dzenis_ska.kvachmach.databinding.FragmentTutorialsBinding


class ProgessFragment : Fragment() {
    lateinit var rootElement: FragmentProgessBinding
    lateinit var viewModel: GameViewModel
    lateinit var adapter: ProgressFragmentAdapter
    private val names = mutableListOf<GamerProgressClass>()
    var id: Int? = 0
    var pref: SharedPreferences? = null
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //in here you can do logic when backPress is clicked

                navController.popBackStack(R.id.tutorialsFragment, true)
                navController.popBackStack(R.id.progessFragment, true)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        pref = this.activity?.getSharedPreferences("FUCK", 0)
        id = pref?.getInt("counter", 1)!!
        Log.d("!!!", id.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootElement = FragmentProgessBinding.inflate(inflater)
        val view = rootElement.root
        viewModel = ViewModelProvider(activity as MainActivity).get(GameViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        adapter = ProgressFragmentAdapter(names, this)
        rootElement.recyclerViewGamers.adapter = adapter
        rootElement.recyclerViewGamers.layoutManager = LinearLayoutManager(activity)
//        adapter.notifyDataSetChanged()

        val clback = NameCallback(this)
        val helper = ItemTouchHelper(clback)
        helper.attachToRecyclerView(rootElement.recyclerViewGamers)

        viewModel.getAllNames()

        viewModel.liveNewName.observe(viewLifecycleOwner, Observer{
            adapter.updateAdapter(it, Constants.CHANGE_GAMERS)
            Log.d("!!!", "PF${it.size}")
        })

        rootElement.floatingActionButton.setOnClickListener(){
            if(rootElement.edEnterName.text.isNotEmpty()){
                val newName = GamerProgressClass(id!!, 1, rootElement.edEnterName.text.toString(), 0,0, 0)
                id = id?.plus(1)
                viewModel.insertNewName(newName)

            }else{
                Toast.makeText(activity as MainActivity, "Введи имя, жопа!", Toast.LENGTH_SHORT).show()
            }
            rootElement.edEnterName.text.clear()
        }


    }
    fun deleteName(id: Int){
//        Log.d("!!!", "id = $id")
        viewModel.deleteName(id)
    }

    override fun onPause() {
        super.onPause()
        saveData(id)
    }
    fun saveData(i: Int?){
        val edit = pref?.edit()
        edit?.putInt("counter", i!!)
        edit?.apply()
    }

    fun setFav(adapterPosition: Int) {
        viewModel.isFav(adapterPosition)
    }

}