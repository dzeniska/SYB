package com.dzenis_ska.kvachmach.LocalModel

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dzenis_ska.kvachmach.GamerProgressClass

@Database(entities = arrayOf(GamerProgressClass::class), version = 1)
abstract class GamersDataBase: RoomDatabase(){
    abstract fun gamersDao(): GamersDao
}