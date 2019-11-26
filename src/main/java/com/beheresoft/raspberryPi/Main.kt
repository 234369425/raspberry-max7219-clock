package com.beheresoft.raspberryPi

import com.beheresoft.big.font.LedBitmap16_16
import com.beheresoft.raspberryPi.io.sr501.GpioLed
import com.beheresoft.raspberryPi.io.sr501.SR501Event
import com.beheresoft.raspberryPi.plugin.Weather
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.wiringpi.Gpio
import java.lang.Exception
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Callable
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    thread {
        while (true) {
            Weather.get()
            sleep(60 * 1000)
        }
    }

    GpioLed.register()
    SR501Event.registerGpio(RaspiPin.GPIO_07, Callable {
        println("open led")
        GpioLed.open()
        return@Callable null
    }, Callable {
        println("close led")
        GpioLed.close()
        return@Callable null
    })

    val led = LedBitmap16_16(16.toShort())
    led.open()
    // 旋转270度，缺省两个屏幕是上下排列，我需要的是左右排
    led.orientation(90)
    sleep(1000)
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
        sleep(100)
    }
}