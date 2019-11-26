package com.beheresoft.raspberryPi.font

import com.beheresoft.raspberryPi.font.model.Chinese16Model
import com.beheresoft.raspberryPi.font.model.English16Model

object FontModel {

    private const val lastEnglishChar = '~'

    fun parse(string: String): ShortArray {
        var charArray = string.toCharArray()
        var bufferSize = calcBufferSize(charArray)
        var str = string
        if(bufferSize % 64 != 0){   //临时处理一下必须为两个两个汉字的问题
            bufferSize += 32
            str += "  "
            charArray = str.toCharArray()
        }
        val short = ShortArray(calcBufferSize(charArray))
        var index = 0
        for (char in charArray) {
            if (char > lastEnglishChar) {
                index = English16Model.process(short, index)
                Chinese16Model.matchOneChar(char).forEach { b ->
                    short[index++] = (b.toInt() and 0xff).toShort()
                }
            } else {
                English16Model.addOneChar(char)
            }
        }
        English16Model.process(short, index)
        return short
    }

    private fun calcBufferSize(charArray: CharArray): Int {
        var bufferSize = 0
        var englishCount = 0
        for (char in charArray) {
            //最后一个可见英文字符
            bufferSize += if (char > lastEnglishChar) {
                if (englishCount % 2 == 1) {
                    englishCount = 0
                    32 + 16
                } else {
                    32
                }
            } else {
                englishCount++
                16
            }
        }
        if (englishCount % 2 == 1) {
            bufferSize += 16
        }
        return bufferSize
    }

}