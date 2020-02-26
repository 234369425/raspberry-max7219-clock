package com.beheresoft.raspberryPi

import com.beheresoft.big.font.MyStrings
import com.beheresoft.raspberryPi.font.FontModel

fun main(args: Array<String>) {
    /**
    CMD.exec("play", "-v", "0.1", "/opt/java/sounds/ybn_stb.wav") {
        println(it)
    }**/
    //val ha = FontModel.parse("不")
    //MyStrings().printByConsole(ha)
    println("production-india".contains("/"))
    val num = 1
    val a = when(num){
        1 -> 2
        2 -> 3
        else -> -1
    }
    println(a)

}