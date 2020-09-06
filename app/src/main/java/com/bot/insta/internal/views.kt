package com.bot.insta.internal

import android.widget.TextView

fun String?.ok() : Boolean = this != null && this == "ok"

fun <T> T.text() where T : TextView = text.trim().toString()