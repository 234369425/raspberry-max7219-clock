package com.beheresoft.raspberryPi.font.model

object English16Model {

    private var chars = ArrayList<Char>()

    fun addOneChar(char: Char) {
        chars.add(char)
    }

    fun process(ref: ShortArray, idx: Int): Int {
        if (chars.isEmpty()) {
            return idx
        }
        if (chars.size % 2 == 1) {
            chars.add(' ')
        }
        val res = ShortArray(chars.size * 16)
        var index = 0
        chars.forEach { c ->
            EnglishW8H16.of(c.toInt()).forEach { s ->
                res[index++] = s
            }
        }
        index = 0
        chars.clear()
        likeChineseProcessMethod(res).forEach { s ->
            ref[idx + index++] = s
        }
        return idx + index
    }

    private fun likeChineseProcessMethod(array: ShortArray): ShortArray {
        val resultBuffer = ShortArray(array.size)
        var index = 0
        for (len in 0 until array.size / 32) {
            for (k in 0..15) {
                for (j in 0..1) {
                    resultBuffer[index++] = array[k + j * 16 + len * 32]
                }
            }
        }
        return resultBuffer
    }

}