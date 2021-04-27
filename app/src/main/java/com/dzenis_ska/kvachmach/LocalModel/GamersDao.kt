package com.dzenis_ska.kvachmach.LocalModel

import androidx.room.*
import com.dzenis_ska.kvachmach.GamerProgressClass

@Dao
interface GamersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProgress(progress: GamerProgressClass)

    @Query("DELETE  FROM progress")
    suspend fun deleteAll()

    @Insert
    suspend fun replase(gamers: MutableList<GamerProgressClass>)

    @Query("SELECT * FROM progress")
    suspend fun getAllNames(): MutableList<GamerProgressClass>

    @Query("DELETE  FROM progress WHERE id LIKE :name")
    suspend fun deleteName(name: Int)

    @Query("UPDATE progress SET fav = :isFav  WHERE id = :id ")
    suspend fun isFav(id: Int, isFav: Int)

    @Query("UPDATE progress SET fav = 0, questions = 0, answers = 0, progress = 0 WHERE id = :id ")
    suspend fun clearProgress(id: Int)

    @Query("UPDATE progress SET questions = :numQuestion, answers = :numAnsvers, progress = :numprogress  WHERE id = :id ")
    suspend fun sendProgress(id: Int, numQuestion: Int, numAnsvers: Int, numprogress: Int)


}