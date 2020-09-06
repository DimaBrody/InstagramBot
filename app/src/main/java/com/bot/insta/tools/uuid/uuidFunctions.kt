package com.bot.insta.tools.uuid

import java.util.*

fun generateUuid(dash: Boolean): String? {
    val uuid = UUID.randomUUID().toString()
    return if (dash) {
        uuid
    } else uuid.replace("-".toRegex(), "")
}

fun generateQueryParams(params: Map<String, String?>): String? {
    val parameters: MutableList<String> =
        ArrayList()
    for (key in params.keys) {
        parameters.add(key + "=" + params[key])
    }
    return if (parameters.size < 2) {
        parameters[0]
    } else {
        var finalResult = ""
        for (q in parameters) {
            finalResult += "$q&"
        }
        finalResult.substring(0, finalResult.length - 2)
    }
}