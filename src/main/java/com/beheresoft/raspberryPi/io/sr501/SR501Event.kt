package com.beheresoft.raspberryPi.io.sr501

import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.Pin
import com.pi4j.io.gpio.RaspiPin
import com.pi4j.io.gpio.PinState
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger
import java.util.concurrent.Callable


object SR501Event {

    /**
     * @see RaspiPin
     */
    fun registerGpio(pin: Pin, high: Callable<Void?>, low: Callable<Void?>) {
        val factory = GpioFactory.getInstance()
        val pir = factory.provisionDigitalInputPin(pin)
        pir.addTrigger(GpioCallbackTrigger(PinState.HIGH, high))
        pir.addTrigger(GpioCallbackTrigger(PinState.LOW, low))
    }

}