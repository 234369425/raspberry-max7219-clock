package com.beheresoft.raspberryPi.verticle

import com.beheresoft.raspberryPi.verticle.bean.DisplayMessage
import com.beheresoft.raspberryPi.verticle.bean.DisplayMessageCodec
import io.vertx.core.AbstractVerticle
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class ClockVerticle : AbstractVerticle() {

    override fun start() {
        val time = SimpleDateFormat("HH:mm:ss")
        val date = SimpleDateFormat("MM月dd日")
        var calendar: Calendar
        var message: DisplayMessage
        thread {
            while (true) {
                calendar = Calendar.getInstance()
                val seconds = calendar.get(Calendar.SECOND)
                val minute = calendar.get(Calendar.MINUTE)

                message = when {
                    minute % 4 == 0 &&
                            seconds in 40..45 -> {
                        DisplayMessage(date.format(System.currentTimeMillis()), level = DisplayMessage.LEVEL.HIGH)
                    }
                    else -> {
                        DisplayMessage(time.format(System.currentTimeMillis()), level = DisplayMessage.LEVEL.LOW)
                    }
                }

                if (seconds == 59) {
                    when (calendar.get(Calendar.HOUR_OF_DAY)) {
                        in 7..8 -> {
                            message.brightness = 3
                        }
                        in 21..24 -> {
                            message.brightness = 2
                        }
                        in 0..7 -> {
                            message.brightness = 1
                        }
                        else -> {
                            message.brightness = 10
                        }
                    }
                }
                vertx.eventBus().send(DISPLAY_ADDRESS, message)
                sleep(100)
            }
        }
    }
}