package com.beheresoft.raspberryPi

import com.beheresoft.big.font.MyStrings
import com.beheresoft.raspberryPi.font.FontModel

fun main(args: Array<String>) {
    /**
    CMD.exec("play", "-v", "0.1", "/opt/java/sounds/ybn_stb.wav") {
        println(it)
    }**/
    val ha = FontModel.parse("不")
    MyStrings().printByConsole(ha)

}