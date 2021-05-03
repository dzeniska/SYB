package com.dzenis_ska.kvachmach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dzenis_ska.kvachmach.LocalModel.LocalModel
import com.dzenis_ska.kvachmach.UI.DialogInstr
import com.dzenis_ska.kvachmach.UI.ProgessFragment
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.ViewModel.GameViewModelFactory
import com.dzenis_ska.kvachmach.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var navController: NavController
    lateinit var rootElement: ActivityMainBinding
    lateinit var viewModel: GameViewModel
    lateinit var anim: Animation
    lateinit var image: ImageView
    val diallogInstr = DialogInstr(this)

    private var job: Job? = null

    private var chooseImageFrag: ProgessFragment? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)

//        anim = AnimationUtils.loadAnimation(this, R.anim.translate)
        image = rootElement.navigationView.getHeaderView(0).findViewById(R.id.imClose)
        image.animation = AnimationUtils.loadAnimation(this, R.anim.translate)



        init()
//        viewModel.getAllNames()
        openCloseDrawer()


    }



    fun openCloseDrawer() {
        //работа при откр-закр drawer
        rootElement.mainContent.intro.constraintLayout.visibility = View.INVISIBLE
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this, rootElement.drawerLayout, rootElement.mainContent.toolbar, R.string.open, R.string.close
        ) {

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset
//                apptool.alpha = (1 - slideOffset)
//                Log.d("!!!", "${slideOffset}")

                if(slideOffset > 0.6f){
                    supportActionBar?.title = resources.getString(R.string.app_name)
                }else if(slideOffset < 0.4f){
                    supportActionBar?.title = viewModel.title
                }

                rootElement.mainContent.intro.constraintLayout.visibility = View.VISIBLE
                rootElement.mainContent.intro.constraintLayout.setTranslationX(slideX)
                rootElement.mainContent.intro.constraintLayout.setScaleX(1 - slideOffset)
                rootElement.mainContent.intro.constraintLayout.setScaleY(1 - slideOffset)
            }
        }
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    fun init() {
        rootElement.drawerLayout.openDrawer(GravityCompat.START)
        navController = findNavController(R.id.navHost)
//        rootElement.mainContent.toolbar.setupWithNavController(navController, rootElement.drawerLayout)
        setSupportActionBar(rootElement.mainContent.toolbar)



        rootElement.navigationView.setNavigationItemSelectedListener(this)

        val localModel = LocalModel(this)
        val factory = GameViewModelFactory(localModel)
        viewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_enter -> {
                navController.navigate(R.id.tutorialsFragment)
                rootElement.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.id_tutorial -> {
                navController.navigate(R.id.gameFragment)
                rootElement.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.id_progress -> {
//                if(viewModel.bool){
                    Log.d("!!!!", "==null")

                job = CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getAllNames()
                    navController.navigate(R.id.progessFragment)
                    count()
                    rootElement.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            R.id.id_instruction -> {
                diallogInstr.createInstrDialog(this)
                rootElement.drawerLayout.closeDrawer(GravityCompat.START)
            }

        }
        return true
    }
    private suspend fun count() = withContext(Dispatchers.IO) {
        delay(450)
    }
}