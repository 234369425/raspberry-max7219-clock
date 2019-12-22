package com.beheresoft.raspberryPi.plugin

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import java.io.File
import java.lang.Exception
import java.net.URL

/**
 *
 */
object Weather {
    var temp = ""
    inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)

    fun get(file: String = "/opt/java/data/weather.json") {
        try {
            val result = File(file).readText()
            val rs = Gson().fromJson(result, Rs::class.java)
            val location = rs.results!![0]["location"]
            val now = rs.results!![0]["now"]
            val text = (now as LinkedTreeMap<*, *>)["text"]
            var temperature = now["temperature"].toString()
            val allLength = text.toString().length
            val len = (4 - 1 - allLength) * 2
            val eLen = len - temperature.length
            var uLen = 0
            for (i in 0 until eLen / 2) {
                uLen++
                temperature = " $temperature"
            }
            for (i in 0 until eLen - uLen) {
                temperature = "$temperature "
            }

            temp = "$text$temperature℃"
            println(temp)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class Rs {
        var results: List<Map<String, Any>>? = null
        var lastUpdate: String? = null
    }

}