package com.beheresoft.raspberryPi.io.max7219

import com.beheresoft.raspberryPi.io.SpiControl

object Control {

    private val spi = SpiControl
    private var rowLed = 8
    private var boardCount = 1
    private var brightnessLevel: Byte = 3.toByte()
    private var buffer: ByteArray = ByteArray(rowLed * boardCount)

    init {
        resetBoard()
    }

    fun setBoardCount(count: Int): Control {
        boardCount = count
        buffer = ByteArray(rowLed * boardCount)
        return this
    }

    fun write(byteArray: ByteArray): Control {
        val res = spi.write(byteArray)
        println(res.toString())
        return this
    }

    fun clear(): Control {
        for (board in 0 until boardCount) {
            for (ledIndex in 0 until rowLed) {
                setBit(board, ledIndex, 0x00.toByte())
            }
        }
        flush()
        return this
    }

    fun close() {
        command(REG_SHUTDOWN, 0x0.toByte())
    }

    fun open() {
        command(REG_SHUTDOWN, 0x1.toByte())
    }

    private fun flush() {
        spi.write(buffer)
    }

    private fun setBit(board: Int, position: Int, data: Byte) {
        val ledIndex = board * rowLed + position
        buffer[ledIndex] = data
    }

    private fun resetBoard() {
        command(REG_SCANLIMIT, 0x7.toByte())
        command(REG_DECODE_MODE, 0x0.toByte())
        command(REG_DISPLAY_TEST, 0x0.toByte())
        this.command(REG_INTENSITY, brightnessLevel)
    }

    private fun setBrightnessLevel(level: Int = 3) {
        brightnessLevel = when {
            level < 0 -> 0
            level > 15 -> 15
            else -> level
        }.toByte()
        this.command(REG_INTENSITY, brightnessLevel)
    }

    private fun command(register: Byte, data: Byte) {
        val len = 2 * boardCount
        val buf = ByteArray(len)
        var i = 0
        while (i < len) {
            buf[i] = register
            buf[i + 1] = data
            i += 2
        }
        spi.write(buf)
    }
}