package com.beheresoft.raspberryPi.font.model

import com.beheresoft.raspberryPi.Application
import java.io.FileInputStream
import java.nio.CharBuffer
import java.nio.charset.Charset

object Chinese16Model {

    private val charset = Charset.forName("GB2312")
    private lateinit var fontCache: ByteArray

    init {
        FileInputStream(Application.fontFile()).use {
            fontCache = ByteArray(it.available())
            it.read(fontCache)
        }
    }

    fun matchOneChar(c: Char): ByteArray {
        val bytes = char2bytes(c)
        val bufferModule = ByteArray(32)
        val highByte = bytes[0].toInt() and 0xff
        val lowByte = bytes[1].toInt() and 0xff
        var offset = (94 * (highByte - 0xa0 - 1) + (lowByte - 0xa0 - 1)) * 32
        if (offset < fontCache.size) {
            for (i in 0..31) {
                val b = fontCache[offset++]
                bufferModule[i] = b
            }
        } else {
            println("$c 缺失~")
        }
        return bufferModule
    }

    private fun char2bytes(c: Char): ByteArray {
        val cb = CharBuffer.allocate(c.toInt())
        cb.put(c)
        cb.flip()
        return charset.encode(cb).array()
    }

}