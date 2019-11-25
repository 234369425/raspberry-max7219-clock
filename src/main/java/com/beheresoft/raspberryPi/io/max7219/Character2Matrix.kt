package com.beheresoft.raspberryPi.io.max7219

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.imageio.ImageIO

object Character2Matrix {
    private lateinit var bImage: BufferedImage
    private lateinit var graphics: Graphics2D
    private var size: Int = 8
    private var bufferLen: Int = 100
    private lateinit var font: Font

    init {
        size()
    }

    fun size(size: Int = 8) {
        this.size = size
        bufferLen = (size * 3.8 + 2 - 1).toInt()
        bImage = BufferedImage(size, size, BufferedImage.TYPE_INT_RGB)
        graphics = bImage.createGraphics()
        graphics.color = Color(255, 255, 255)
        graphics.background = Color.BLACK
        graphics.font = loadFont()
    }

    fun font(fontName: String) {
        this.font = Font(fontName, Font.PLAIN, size + 1)
        graphics.font = this.font
    }


    fun convent(char: Char): ByteArray {
        graphics.clearRect(0, 0, size, size)
        val text = ByteArray(size)
        graphics.drawString(char.toString(), 0, size - 1)
        for (x in 0 until size) {
            var byte = 0x0
            val currentBit = 0x1
            for (y in 0 until 8) {
                if (bImage.getRGB(y, x) > -16777216) {
                    byte += currentBit
                }
                currentBit shl 1
            }
            text[x] = byte.toByte()
        }
        return text
    }

    fun saveToDisk(fileName: String) {
        val file = File(fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        ImageIO.write(bImage, "PNG", FileOutputStream(file))
    }

    private fun loadFont(): Font {
        with(FileInputStream(File("/opt/java/fonts/msyh.ttc"))) {
            return Font.createFont(Font.TRUETYPE_FONT, this)
        }
    }


}