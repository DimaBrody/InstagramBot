package com.bot.insta.data.repository

import com.bot.insta.data.UserDao
import com.bot.insta.data.model.db.UserData

interface DatabaseRepository {
    val userDao : UserDao

     fun insertUser(user: UserData)

     fun fetchUser(pk: Long, onFetched: (UserData?) -> Unit)
}