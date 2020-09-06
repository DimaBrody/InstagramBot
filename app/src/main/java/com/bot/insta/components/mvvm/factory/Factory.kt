package com.bot.insta.components.mvvm.factory

interface Factory {
    fun <F> create(modelClass: Class<F>): F
}