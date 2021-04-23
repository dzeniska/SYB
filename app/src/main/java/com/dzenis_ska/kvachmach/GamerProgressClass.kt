package com.dzenis_ska.kvachmach

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class  GamerProgressClass (
    @PrimaryKey val id: Int,
    val fav: Int = 0,
    val name: String,
    val questions: Int,
    val answers: Int,
    val progress: Int
)
