package com.dzenis_ska.kvachmach.LocalModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzenis_ska.kvachmach.GamerProgressClass

@Dao
interface GamersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProgress(progress: GamerProgressClass)

    @Query("SELECT * FROM progress")
    suspend fun getAllNames(): MutableList<GamerProgressClass>

    @Query("DELETE  FROM progress WHERE id LIKE :name")
    suspend fun deleteName(name: Int)

    @Query("UPDATE progress SET fav = :isFav  WHERE id = :id ")
    suspend fun isFav(id: Int, isFav: Int)

    @Query("UPDATE progress SET questions = :numQuestion, answers = :numAnsvers, progress = :numprogress  WHERE id = :id ")
    suspend fun sendProgress(id: Int, numQuestion: Int, numAnsvers: Int, numprogress: Int)


}