package com.beheresoft.raspberryPi

import com.beheresoft.utils.CMD

fun main(args: Array<String>) {
    CMD.exec("play","-v","0.1","/opt/java/sounds/ybn_stb.wav"){
        println(it)
    }
}