package com.beheresoft.raspberryPi.io.max7219.util

import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.GraphicsEnvironment
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import javax.imageio.ImageIO

object Character2Matrix {
    private lateinit var bImage: BufferedImage
    private lateinit var graphics: Graphics2D
    private var width = 8
    private var height = 8
    private var bufferLen: Int = 100
    private lateinit var font: Font
    private var fontFile: String? = null
    private var fontName: String = "Microsoft YaHei UI Light"
    private var model = MODEL.UP_TO_DOWN
    private var saveBytesFile = ""
    private lateinit var raf: RandomAccessFile
    private val max7219Pixel = 8

    init {
        size()
    }

    fun setSaveFile(path: String): Character2Matrix {
        this.saveBytesFile = path
        val path = Paths.get(path)
        if (Files.deleteIfExists(path)) {

        }
        raf = RandomAccessFile(Files.createFile(path).toFile(), "rw")
        return this
    }

    fun size(width: Int = 8, height: Int = 8): Character2Matrix {
        this.width = width
        this.height = height
        bufferLen = width * height
        bImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        graphics = bImage.createGraphics()
        graphics.color = Color(255, 255, 255)
        graphics.background = Color.BLACK
        graphics.font = loadFont()
        return this
    }

    fun font(font: String, file: Boolean = false) {
        if (file) {
            this.fontFile = font
        } else {
            this.fontName = font
        }
        this.font = loadFont()
        graphics.font = this.font
    }

    /**
     * 这么拆
     *
     *  1   |  2
     *  ————
     *  3  |  4
     */
    fun flush(str: String): Character2Matrix {
        val y = height / 8
        graphics.drawString(str, 0, height - y)

        val rows = height / max7219Pixel
        val cells = height / max7219Pixel
        /*
         *
         */
        if (model == MODEL.UP_TO_DOWN) {
            for (column in 0 until cells) {
                for (row in 0 until rows) {
                    for (x in 0 until max7219Pixel) {
                        var byte = 0x0
                        for (y in 0 until max7219Pixel) {
                            if (bImage.getRGB(x + row * max7219Pixel, y + column * max7219Pixel) > -16777216) {
                                byte += 0x1
                            }
                            byte = byte shl 1
                        }
                        raf.writeShort(byte)
                    }
                }
            }
        } else if (model == MODEL.LEFT_TO_RIGHT) {
            for (column in 0 until cells) {
                for (row in 0 until rows) {
                    for (y in 0 until max7219Pixel) {
                        var byte = 0x0
                        for (x in 0 until max7219Pixel) {
                            if (bImage.getRGB(x + row * max7219Pixel, y + column * max7219Pixel) > -16777216) {
                                byte += 0x1
                            }
                            byte = byte shl 1
                        }
                        raf.writeShort(byte)
                    }
                }
            }
        }
        return this
    }

    fun finish() {
        raf.close()
    }


    fun convent(char: Char): ByteArray {
        graphics.clearRect(0, 0, width, height)
        val text = ByteArray(width * height)
        val y = height / 8
        graphics.drawString(char.toString(), 0, height - y)
        for (x in 0 until width) {
            var byte = 0x0
            val currentBit = 0x1
            for (y in 0 until height) {
                if (bImage.getRGB(y, x) > -16777216) {
                    byte += currentBit
                }
                byte = byte shl 1
            }
            text[x] = byte.toByte()
        }
        return text
    }

    fun saveImageToDisk(fileName: String) {
        val file = File(fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        ImageIO.write(bImage, "PNG", FileOutputStream(file))
    }

    fun displayAvirableFonts() {
        val e = GraphicsEnvironment.getLocalGraphicsEnvironment()
        for (s in e.availableFontFamilyNames) {
            println(s)
        }
    }

    private fun loadFont(): Font {
        if (fontFile == null) {
            return Font(fontName, Font.PLAIN, height)
        }
        with(FileInputStream(File(fontFile))) {
            return Font.createFont(Font.TRUETYPE_FONT, this)
        }
    }

    enum class MODEL {
        UP_TO_DOWN,
        LEFT_TO_RIGHT
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val cm = Character2Matrix
        cm.displayAvirableFonts()
        cm.size(16, 16)
        cm.setSaveFile("d:/lib")
        cm.convent('你')
        cm.saveImageToDisk("d:/test.png")

        val gb2312 = Charset.forName("GB2312")

        return
        for (bh in 0xA1..0xF7) {
            for (bl in 0xA0..0xFE) {
                val s = String(byteArrayOf(bh.toByte(), bl.toByte()), gb2312)
                cm.flush(s)
            }
        }
        cm.finish()

    }


}