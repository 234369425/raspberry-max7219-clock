package com.beheresoft.raspberryPi

import com.beheresoft.big.font.LedBitmap16_16
import com.beheresoft.big.font.MyStrings
import com.beheresoft.raspberryPi.font.FontModel
import com.beheresoft.raspberryPi.io.max7219.Controller

object Application {

    fun debug() = "true" == System.getProperty("debug")

    fun fontFile() = System.getProperty("font.location", "") + "/" + System.getProperty("font.name", "HZK16C")

    fun same(b1: ShortArray, b2: ShortArray) {
        if (b1.size != b2.size) {
            println("different")
            return
        }
        for (i in b1.indices) {
            if (b1[i] != b2[i]) {
                println("different")
                return
            }
        }
        println("same")
    }

    fun printBytes(bytes: ByteArray) {
        if (debug()) {
            println()
            for (b in bytes) {
                print("$b,")
            }
            println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-")
        }
    }

    fun printByControl(shortArray: ShortArray) {
        val key = arrayOf(0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01)

        if (shortArray.size % 32 != 0) {
            System.out.println("字符串缓存返回错误（不是32的倍数）");
            return
        }
        for (len in 0 until shortArray.size / 32) {
            for (k in 0..15) {
                for (j in 0..1) {
                    val byteDate = shortArray[k + 16 * j + len * 32]
                    for (i in 0..7) {
                        val flag = byteDate.toInt() and key[i]
                        System.out.printf("%s", if (flag == 0) " " else "●")
                    }
                }
                System.out.println();
            }
            System.out.println("************************");
        }
    }

    fun printShort(shortArray: ShortArray) {
        if (debug()) {
            for (s in shortArray) {
                print(s.toByte().toString() + ",")
            }
            println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("debug", "true")
        System.setProperty("font.location", "D:\\intellij\\clock\\fonts")

        val string = "你AA"
        val b1 = FontModel.parse(string)
        val b2 = MyStrings().getStringBuffer(string)
        Application.same(b1, b2)

        val led = LedBitmap16_16(16.toShort())
        led.orientation(90)
        val r = led.splitIntoTwoScreen(b1)

        val controller = Controller()
        val c = controller.splitDataIntoRows(b2)
        printBytes(c)

    }

}