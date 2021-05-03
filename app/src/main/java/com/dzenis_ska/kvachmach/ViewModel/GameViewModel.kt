package com.dzenis_ska.kvachmach.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dzenis_ska.kvachmach.GamerProgressClass
import com.dzenis_ska.kvachmach.LocalModel.LocalModel
import kotlinx.coroutines.*

class GameViewModel(val localModel: LocalModel) : ViewModel() {
    var job: Job? = null

    val array = mutableListOf<String>()

    var bool: Boolean = true

    var title: String = "Shake Your Brain"

    val scope = CoroutineScope(Dispatchers.IO)

    var repl2: GamerProgressClass? = null

    var liveData = MutableLiveData<String>()

    val getAllG = mutableListOf<GamerProgressClass>()

    var liveNewName = MutableLiveData<MutableList<GamerProgressClass>>()

    var index: Int = 0

    fun getQuestion(): String {


            val quantity = array.size
        val q = array[(0 until quantity).random()]
            val index = array.indexOf(q)
            array.removeAt(index)

        Log.d("!!!q", q.toString())
        Log.d("!!!q", array.size.toString())

        return q
    }


    fun insertNewName(progress: GamerProgressClass) {
        scope.launch {
            localModel.insertNewName(progress)
            getAllG.add(progress)
            index = 0
            liveNewName.postValue(getAllG)
        }
    }

    suspend fun getAllNames() = withContext(Dispatchers.IO) {
        scope.launch {
            val data = localModel.getAllNames()
            getAllG.clear()
            getAllG.addAll(data)
            index = 0
            liveNewName.postValue(data)

        }
    }

    fun deleteName(num: Int) {
        scope.launch {
            val id = getAllG[num].id
            localModel.deleteName(id)
            getAllG.removeAt(num)
//            getAll.addAll(data)
            index = 0
            liveNewName.postValue(getAllG)

        }
    }

    fun clearProgress(num: Int) {
        scope.launch {
            val repl = getAllG.get(num)
            val rev = GamerProgressClass(repl.id, 0, repl.name, 0, 0, 0)
            getAllG.removeAt(num)
            getAllG.add(num, rev)
            localModel.clearProgress(repl.id)
            liveNewName.postValue(getAllG)
        }
    }

    fun isFav(num: Int) {
        scope.launch {
            val id = getAllG[num].id
            val isFav = getAllG[num].fav
            val repl = getAllG.get(num)

            if (isFav == 1) {
                repl2 = GamerProgressClass(repl.id, 0, repl.name, repl.questions, repl.answers, repl.progress)
                getAllG.removeAt(num)
                getAllG.add(num, repl2!!)
                localModel.isFav(id, 0)
            } else {
                repl2 = GamerProgressClass(repl.id, 1, repl.name, repl.questions, repl.answers, repl.progress)
                getAllG.removeAt(num)
                getAllG.add(num, repl2!!)
                localModel.isFav(id, 1)
            }
            index = 0
            liveNewName.postValue(getAllG)

        }
    }

    fun sendProgress(id: Int, numQuestion: Int, numAnsvers: Int, numprogress: Int) {
        scope.launch {

            localModel.sendProgress(id, numQuestion, numAnsvers, numprogress)

        }
    }

    fun replase(startPos: Int, targetPos: Int) {
        scope.launch {
            val target = getAllG[targetPos]
            val targetId = getAllG[targetPos].id
            val targetFav = getAllG[targetPos].fav
            val targetName = getAllG[targetPos].name
            val targetQuestions = getAllG[targetPos].questions
            val targetAnswers = getAllG[targetPos].answers
            val targetProgress = getAllG[targetPos].progress

            val start = getAllG[startPos]
            val startId = getAllG[startPos].id
            val startFav = getAllG[startPos].fav
            val startName = getAllG[startPos].name
            val startQuestions = getAllG[startPos].questions
            val startAnswers = getAllG[startPos].answers
            val startProgress = getAllG[startPos].progress

            getAllG[targetPos] = GamerProgressClass(targetId, startFav, startName, startQuestions, startAnswers, startProgress)
            getAllG[startPos] = GamerProgressClass(startId, targetFav, targetName, targetQuestions, targetAnswers, targetProgress)
            localModel.replace(getAllG)
            index = 1
            liveNewName.postValue(getAllG)

        }
    }
}
