package com.bot.insta.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_database")
data class UserData(
    @PrimaryKey(autoGenerate = false)
    val pk: Long = 0,
    var count: Int = 0
)