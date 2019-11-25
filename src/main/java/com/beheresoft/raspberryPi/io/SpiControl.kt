package com.beheresoft.raspberryPi.io

import com.pi4j.io.spi.SpiChannel
import com.pi4j.io.spi.SpiDevice
import com.pi4j.io.spi.SpiFactory
import java.nio.ByteBuffer


object SpiControl {

    private val spi: SpiDevice = SpiFactory.getInstance(SpiChannel.CS0,
            SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
            SpiDevice.DEFAULT_SPI_MODE)

    fun write(array: ByteArray): ByteBuffer? {
        val buffer = spi.write(ByteBuffer.wrap(array))
        println(String(buffer.array()))
        return buffer
    }

}