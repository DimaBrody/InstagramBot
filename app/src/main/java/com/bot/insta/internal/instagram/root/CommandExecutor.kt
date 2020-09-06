package com.bot.insta.internal.instagram.root

import com.bot.insta.data.variables.AppValues
import java.io.IOException
import java.util.ArrayList

class CommandExecutor : ExecuteAsRootBase() {

    private var currentCommand: Command = Command.TAP

    private val currentX: Int
        get() = AppValues.screenDimensions[0]

    private val currentY: Int
        get() = AppValues.screenDimensions[1]

    private val centerX : Int
        get() = if(currentX  != 0) currentX/2 else 100

    private val bottomY : Int
        get() = if(currentY != 0) currentY - 500 else 450

    private val swipeLength: Int
        get() = 120

    override fun getCommandsToExecute(): ArrayList<String> =
        when (currentCommand) {
            Command.TAP->arrayListOf("input tap 40 140")
            Command.SWIPE-> arrayListOf("input swipe $centerX $bottomY $centerX $swipeLength 200")
        }

    fun executeTap(){
        currentCommand = Command.TAP
        executeCommands()
    }

    fun executeSwipe(){
        currentCommand = Command.SWIPE
        executeCommands()
    }

    private fun executeCommands() {
        if (canRunRootCommands())
            execute()
        else throw IOException("No root!")
    }


    companion object {
        private var _instance: CommandExecutor? = null

        val instance: CommandExecutor
            get() = _instance ?: CommandExecutor().also {
                _instance = it
            }
    }

    enum class Command {
        TAP, SWIPE
    }

}