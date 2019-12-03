package com.beheresoft.raspberryPi.verticle

import com.beheresoft.big.font.LedBitmap16_16
import com.beheresoft.raspberryPi.plugin.Weather
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DisplayVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        val led = LedBitmap16_16(16.toShort())
        led.open()
        led.orientation(90)
        Thread.sleep(1000)
        led.open()
        val time = SimpleDateFormat("HH:mm:ss")
        val date = SimpleDateFormat("MM月dd日")
        var calendar: Calendar
        while (true) {
            calendar = Calendar.getInstance()
            val seconds = calendar.get(Calendar.SECOND)
            val minute = calendar.get(Calendar.MINUTE)
            when {
                minute % 5 == 0 &&
                        seconds in 40..44 -> {
                    led.showRightNow16(date.format(System.currentTimeMillis()))
                }
                minute % 3 == 0 &&
                        seconds in 45..58 -> {
                    try {
                        led.showRightNow16(Weather.temp)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else -> {
                    led.showRightNow16(time.format(System.currentTimeMillis()))
                }
            }

            //led.showRightNow16(time.format(System.currentTimeMillis()), true)
            if (seconds == 59) {
                when (calendar.get(Calendar.HOUR_OF_DAY)) {
                    in 7..8 -> {
                        led.brightness(3)
                    }
                    in 21..24 -> {
                        led.brightness(2)
                    }
                    in 0..7 -> {
                        led.brightness(1)
                    }
                    else -> {
                        led.brightness(5)
                    }
                }
            }
            led.flush()
            Thread.sleep(100)
        }
    }
}