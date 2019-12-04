package com.beheresoft.raspberryPi.verticle

import com.beheresoft.big.font.LedBitmap16_16
import com.beheresoft.raspberryPi.verticle.bean.DisplayMessage
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import java.lang.Thread.sleep
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

class DisplayVerticle : AbstractVerticle() {

    private val blockingDeque = LinkedBlockingQueue<DisplayMessage>()

    override fun start(startFuture: Future<Void>?) {
        val led = LedBitmap16_16(16.toShort())
        led.open()
        led.orientation(90)
        led.reset()

        vertx.eventBus().consumer<DisplayMessage>(DISPLAY_ADDRESS) {
            val message = it.body()
            if (blockingDeque.isNotEmpty() && message.level == DisplayMessage.LEVEL.LOW) {
                return@consumer
            }
            blockingDeque.add(message)
        }

        thread {
            val sleepMills: Long = 100
            var delay: Long = 0
            while (true) {
                sleep(sleepMills)
                if (delay > 0) {
                    delay -= sleepMills
                    continue
                }
                val message = blockingDeque.take()
                if (message.brightness != null) {
                    led.brightness(message.brightness!!)
                }
                delay = message.millis
                led.showRightNow16(message.message)
                led.flush()
            }
        }

    }

}

const val DISPLAY_ADDRESS = "BUS:EVENT:DISPLAY"

