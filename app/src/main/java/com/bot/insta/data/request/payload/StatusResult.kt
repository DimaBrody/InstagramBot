package com.bot.insta.data.request.payload

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class StatusResult(
     var status: String = "",
     var message: String = ""
)