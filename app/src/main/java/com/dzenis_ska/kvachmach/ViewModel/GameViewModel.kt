package com.dzenis_ska.kvachmach.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dzenis_ska.kvachmach.GamerProgressClass
import com.dzenis_ska.kvachmach.LocalModel.LocalModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GameViewModel(val localModel: LocalModel): ViewModel(){
    var job: Job? = null


    val scope  = CoroutineScope(Dispatchers.IO)

    var repl2: GamerProgressClass? = null

    var liveData = MutableLiveData<String>()

    val getAll = mutableListOf<GamerProgressClass>()

    var liveNewName = MutableLiveData<MutableList<GamerProgressClass>>()
    var liveNewName2 = MutableLiveData<MutableList<GamerProgressClass>>()



    fun insertNewName(progress: GamerProgressClass){
        scope.launch {
            localModel.insertNewName(progress)
            getAll.add(progress)
            liveNewName.postValue(getAll)
        }
    }

    fun getAllNames(){
        scope.launch {
            val data = localModel.getAllNames()
            getAll.clear()
            getAll.addAll(data)
            liveNewName.postValue(data)

        }
    }
    fun getAllNames2(){
        scope.launch {
            val data = localModel.getAllNames()
            getAll.clear()
            getAll.addAll(data)
            liveNewName2.postValue(data)

        }
    }
    fun deleteName(num: Int){
        scope.launch {
            val id = getAll[num].id
            localModel.deleteName(id)
            getAll.removeAt(num)
//            getAll.addAll(data)
            liveNewName.postValue(getAll)

        }
    }
    fun isFav(num: Int){
        scope.launch {
            val id = getAll[num].id
            val isFav = getAll[num].fav
            val repl = getAll.get(num)

            if (isFav == 1){
                repl2 = GamerProgressClass(repl.id, 0, repl.name, repl.questions, repl.answers, repl.progress)
                getAll.removeAt(num)
                getAll.add(num, repl2!!)
                localModel.isFav(id, 0)
            }else{
                repl2 = GamerProgressClass(repl.id, 1, repl.name, repl.questions, repl.answers, repl.progress)
                getAll.removeAt(num)
                getAll.add(num, repl2!!)
                localModel.isFav(id, 1)
            }
            liveNewName.postValue(getAll)

        }
    }

    fun sendProgress(id: Int, numQuestion: Int, numAnsvers: Int, numprogress: Int) {
        scope.launch {

            localModel.sendProgress(id, numQuestion, numAnsvers, numprogress)

        }
    }
}
