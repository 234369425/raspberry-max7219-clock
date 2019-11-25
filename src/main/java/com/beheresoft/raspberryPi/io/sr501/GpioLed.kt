package com.beheresoft.raspberryPi.io.sr501

import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.GpioPinDigitalOutput
import com.pi4j.io.gpio.Pin
import com.pi4j.io.gpio.RaspiPin

object GpioLed {

    private var pin: Pin = RaspiPin.GPIO_29
    private lateinit var controller: GpioPinDigitalOutput


    fun register(pin: Pin = RaspiPin.GPIO_29): GpioLed {
        this.pin = pin
        controller = GpioFactory.getInstance().provisionDigitalOutputPin(pin)
        controller.low()
        return this
    }

    fun open() {
        controller.high()
    }

    fun close() {
        controller.low()
    }

}