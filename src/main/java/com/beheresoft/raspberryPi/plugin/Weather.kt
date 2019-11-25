package com.beheresoft.raspberryPi.plugin

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import java.io.File
import java.lang.Exception
import java.net.URL

object Weather {
    var temp = ""
    inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)

    fun get() {
        try {
            val result = File("/opt/java/app/weather.json").readText()
            val rs = Gson().fromJson(result, Rs::class.java)
            val location = rs.results!![0]["location"]
            val name = (location as Map<*, *>)["name"]
            val now = rs.results!![0]["now"]
            val text = (now as LinkedTreeMap<*, *>)["text"]
            val temperature = now["temperature"]
            temp = if (temperature.toString().length < 3) {
                "$text $temperature ℃..."
            } else {
                "$text$temperature℃..."
            }
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