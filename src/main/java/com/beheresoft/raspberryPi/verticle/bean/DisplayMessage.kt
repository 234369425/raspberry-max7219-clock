package com.beheresoft.raspberryPi.verticle.bean

class DisplayMessage(val message: String, val millis: Long = 100, val level: LEVEL = LEVEL.NORMAL, var brightness: Byte? = null) {

    enum class LEVEL {
        LOW,
        NORMAL,
        HIGH
    }

}