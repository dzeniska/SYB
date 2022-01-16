package com.dzenis_ska.kvachmach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.dzenis_ska.kvachmach.LocalModel.LocalModel
import com.dzenis_ska.kvachmach.UI.DialogInstr
import com.dzenis_ska.kvachmach.ViewModel.GameViewModel
import com.dzenis_ska.kvachmach.ViewModel.GameViewModelFactory
import com.dzenis_ska.kvachmach.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var attempt = 0
    lateinit var navController: NavController
    lateinit var rootElement: ActivityMainBinding
    lateinit var image: ImageView
    val diallogInstr = DialogInstr(this)

    private var job: Job? = null

//    lateinit var viewModel: GameViewModel
    val viewModel: GameViewModel by viewModels{
        GameViewModelFactory(LocalModel(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        setContentView(rootElement.root)

        image = rootElement.navigationView.getHeaderView(0).findViewById(R.id.imClose)
        image.animation = AnimationUtils.loadAnimation(this, R.anim.translate)
//        val localModel = LocalModel(this)
//        val factory = GameViewModelFactory(localModel)
//        viewModel = ViewModelProvider(this, factory).get(GameViewModel::class.java)

        init()
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
                    if(supportActionBar?.title != resources.getString(R.string.add_gamer))
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

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_enter -> {
                job = CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getAllNames()
                    job = null
                }
                navigateTo(R.id.tutorialsFragment)
//                navController.navigate(R.id.tutorialsFragment)
//                navController.popBackStack(R.id.tutorialsFragment, true)
                rootElement.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.id_tutorial -> {
                navigateTo(R.id.gameFragment)
//                navController.navigate(R.id.gameFragment)
//                navController.popBackStack(R.id.gameFragment, true)
                rootElement.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.id_progress -> {
                job = CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getAllNames()
                    navigateTo(R.id.progessFragment)
//                    navController.navigate(R.id.progessFragment)
//                    navController.popBackStack(R.id.progessFragment, true)
                    count(450)
                    rootElement.drawerLayout.closeDrawer(GravityCompat.START)
                    job = null
                }
            }
            R.id.id_instruction -> {
                diallogInstr.createInstrDialog(this)
            }

        }
        return true
    }

    private fun navigateTo(id: Int) {
        var bool = false
        navController.backQueue.forEach {
            if(it.destination.id == id) bool = true
            Log.d("!!!navController", "${it.destination.label} _ ${bool}")
//            when(it.destination.label) {
//                "fragment_game" -> {
//                    idToBack = R.id.gameFragment
////                    return@forEach
//                }
//                "fragment_progess" -> {
//                    idToBack = R.id.progessFragment
////                    return@forEach
//
//                }
//                "fragment_game" -> {
//                    idToBack = R.id.gameFragment
////                    return@forEach
//                }
//            }
        }

        navController.navigate(id, null, navOptions {
//            if(idToBack != null){
                popUpTo(id) { inclusive = bool }
//            }
        })
    }

    private suspend fun count(timeMillis: Long) = withContext(Dispatchers.IO) {
        delay(timeMillis)
        attempt = 0
    }

    override fun onBackPressed() {
        CoroutineScope(Dispatchers.Main).launch {
            if(attempt == 0){
                Toast.makeText(applicationContext, "Нет, не жми сюда второй раз подряд!",
                        Toast.LENGTH_SHORT).show()
                attempt++
                        count(1500)

            }else if(attempt == 1){
                Toast.makeText(applicationContext, "Я предупреждал...",
                        Toast.LENGTH_SHORT).show()
                super.onBackPressed()
            }
        }
    }
}