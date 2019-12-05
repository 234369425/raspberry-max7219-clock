package com.beheresoft.raspberryPi

import com.beheresoft.raspberryPi.io.sr501.GpioLed
import com.beheresoft.raspberryPi.io.sr501.SR501Event
import com.beheresoft.raspberryPi.scheduler.QuartzVerticle
import com.beheresoft.raspberryPi.verticle.ClockVerticle
import com.beheresoft.raspberryPi.verticle.DisplayVerticle
import com.beheresoft.raspberryPi.verticle.WeatherVerticle
import com.beheresoft.raspberryPi.verticle.bean.DisplayMessage
import com.beheresoft.raspberryPi.verticle.bean.DisplayMessageCodec
import com.pi4j.io.gpio.RaspiPin
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.logging.LoggerFactory
import java.util.concurrent.Callable

class MainVerticle : AbstractVerticle() {

    private val logger = LoggerFactory.getLogger(MainVerticle::class.java)

    override fun start() {
        vertx.eventBus().registerDefaultCodec(DisplayMessage::class.java, DisplayMessageCodec())
        vertx.deployVerticle(QuartzVerticle::class.java, createWorkerOption()) {
            if (it.succeeded()) {
                logger.info("scheduler deploy success~~")
            } else {
                it.cause().printStackTrace()
            }
        }
        Application.setConfig(config())

        vertx.deployVerticle(WeatherVerticle::class.java, createWorkerOption())
        vertx.deployVerticle(ClockVerticle::class.java, createWorkerOption())
        vertx.deployVerticle(DisplayVerticle::class.java, createWorkerOption())
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