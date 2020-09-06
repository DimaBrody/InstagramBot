package com.bot.insta.internal.instagram.functions

import com.bot.insta.internal.conditions.ButtonCondition
import com.bot.insta.internal.model.TabContainer

fun onSetup(tabContainer: TabContainer): (ButtonCondition, Int) -> Boolean = { button, condition ->
    button(condition)
}