package com.bot.insta.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bot.insta.data.model.db.UserData

@Dao
interface UserDao {

    @Query("select * from users_database where pk == :pk")
    fun fetchUser(pk: Long) : UserData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(data: UserData)
}