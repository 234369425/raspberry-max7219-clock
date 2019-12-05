package com.beheresoft.raspberryPi.verticle

import com.beheresoft.big.font.LedBitmap16_16
import com.beheresoft.raspberryPi.DISPLAY_ADDRESS
import com.beheresoft.raspberryPi.REFRESH_RATE
import com.beheresoft.raspberryPi.verticle.bean.DisplayMessage
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import java.lang.Thread.sleep
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class DisplayVerticle : AbstractVerticle() {

    private val blockingDeque = LinkedBlockingQueue<DisplayMessage>()
    private val displaying = AtomicBoolean(false)

    override fun start() {
        val led = LedBitmap16_16(16.toShort())
        led.open()
        led.orientation(90)
        led.reset()

        vertx.eventBus().consumer<DisplayMessage>(DISPLAY_ADDRESS) {
            val message = it.body()
            if (displaying.get() && blockingDeque.isNotEmpty()
                    && message.level == DisplayMessage.LEVEL.LOW) {
                return@consumer
            }
            blockingDeque.add(message)
        }

        thread {
            var delay: Long = 0
            while (true) {
                sleep(REFRESH_RATE)
                if (delay > 0) {
                    delay -= REFRESH_RATE
                    continue
                }
                displaying.set(false)
                val message = blockingDeque.take()
                if (message.brightness != null) {
                    led.brightness(message.brightness!!)
                }
                delay = message.millis
                led.showRightNow16(message.message)
                displaying.set(true)
                led.flush()
            }
        }

    }

}


