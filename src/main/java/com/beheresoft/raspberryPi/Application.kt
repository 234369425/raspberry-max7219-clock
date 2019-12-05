package com.beheresoft.raspberryPi

import com.beheresoft.big.font.LedBitmap16_16
import com.beheresoft.big.font.MyStrings
import com.beheresoft.raspberryPi.font.FontModel
import com.beheresoft.raspberryPi.io.max7219.BitOperation.printByConsole
import com.beheresoft.raspberryPi.io.max7219.Controller
import io.vertx.core.json.JsonObject

object Application {

    private var debug = false
    var fontFile = ""

    fun debug() = debug

    fun setConfig(config: JsonObject) {
        debug = config.getString("debug", "false") == "true"
        val font = config.getJsonObject("font")
        if (font != null) {
            fontFile = font.getString("location") + "/" + font.getString("name")
        }
    }

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
        System.setProperty("font.location", "I:\\clock\\fonts")

        val string = "他AA"
        val b1 = FontModel.parse(string)
        val b2 = MyStrings().getStringBuffer(string)
        same(b1, b2)
        printByConsole(b1)

        val led = LedBitmap16_16(16.toShort())
        led.orientation(90)
        val r = led.splitShortIntoTwoScreen(b1)
        printByConsole(r)
        val controller = Controller()
        val c = controller.splitDataIntoRows(b2)
        printBytes(c)

    }

}