package com.bot.insta.data.repository

import com.bot.insta.data.UserDao
import com.bot.insta.data.model.db.UserData

class DatabaseRepositoryImpl(
    override val userDao: UserDao
) : DatabaseRepository {

    override fun insertUser(user: UserData) {
        userDao.insertUser(user)
    }

    override fun fetchUser(pk: Long, onFetched: (UserData?) -> Unit) {
        onFetched(userDao.fetchUser(pk))
    }
}