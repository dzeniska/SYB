package com.dzenis_ska.kvachmach.LocalModel

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.dzenis_ska.kvachmach.GamerProgressClass

class LocalModel(context: Context) {
    private val dataBase: GamersDataBase = Room.databaseBuilder(context, GamersDataBase::class.java, "dataBase").build()

    suspend fun deleteName(name: Int){
        dataBase.gamersDao().deleteName(name)
    }
    suspend fun insertNewName(progress: GamerProgressClass){
        dataBase.gamersDao().insertProgress(progress)
    }
    suspend fun replace(gamerss: MutableList<GamerProgressClass>){
        Log.d("!!!repll", "$gamerss")
        dataBase.gamersDao().deleteAll()
        dataBase.gamersDao().replase(gamerss)
    }
    suspend fun getAllNames(): MutableList<GamerProgressClass>{
        return dataBase.gamersDao().getAllNames()
    }
    suspend fun isFav(id: Int, isFav: Int){
        dataBase.gamersDao().isFav(id, isFav)
    }

    suspend fun sendProgress(id: Int, numQuestion: Int, numAnsvers: Int, numprogress: Int) {
        dataBase.gamersDao().sendProgress(id, numQuestion, numAnsvers, numprogress)
    }
}