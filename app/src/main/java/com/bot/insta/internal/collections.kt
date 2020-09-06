package com.bot.insta.internal

fun <T> MutableList<T>.addNew(items: Iterable<T>){
    clear()
    addAll(items)
}

private fun iterate(
    iteration: (nextIteration: () -> Unit) -> Unit
) {
    lateinit var nextIteration: () -> Unit
    nextIteration = {
        iteration(nextIteration)
    }
    nextIteration()
}

fun <T> Iterable<T>.iterate(
    iteration: (item: T, nextIteration: () -> Unit) -> Unit,
    complete: () -> Unit
) {
    val iterator = iterator()
    iterate { next ->
        if (iterator.hasNext()) {
            val item = iterator.next()
            iteration(item, next)
        } else complete()
    }
}