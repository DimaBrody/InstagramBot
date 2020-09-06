package com.bot.insta.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bot.insta.data.model.db.UserData

@Database(
    entities = [UserData::class],
    version = 2
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        private val LOCK = Any()

        operator fun invoke(
            context: Context
        ) = instance ?: synchronized(LOCK){
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "users.db"
            ).build().also { instance = it }
        }
    }
}