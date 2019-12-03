package com.beheresoft.raspberryPi

import com.beheresoft.raspberryPi.io.sr501.GpioLed
import com.beheresoft.raspberryPi.io.sr501.SR501Event
import com.beheresoft.raspberryPi.plugin.Weather
import com.beheresoft.raspberryPi.scheduler.QuartzVerticle
import com.beheresoft.raspberryPi.verticle.WeatherVerticle
import com.pi4j.io.gpio.RaspiPin
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.logging.LoggerFactory
import java.lang.Thread.sleep
import java.util.concurrent.Callable
import kotlin.concurrent.thread

class MainVerticle : AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(MainVerticle::class.java)

    override fun start(startFuture: Future<Void>?) {
        vertx.deployVerticle(QuartzVerticle::class.java, createWorkerOption()) {
            if (it.succeeded()) {
                logger.info("scheduler deploy success~~")
            } else {
                it.cause().printStackTrace()
            }
        }

        vertx.deployVerticle(WeatherVerticle::class.java, createWorkerOption())
        logger.info("started")
    }

    private fun createWorkerOption(): DeploymentOptions {
        val options = DeploymentOptions()
        options.config = config()
        options.isWorker = true
        return options
    }
}

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


}