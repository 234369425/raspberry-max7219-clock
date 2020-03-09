package com.beheresoft.raspberryPi

import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.lang.Thread.sleep
import java.net.URL

fun main(args: Array<String>) {
    for (i in 36..121) {
        val document = Jsoup.connect("https://bing.ioliu.cn/ranking?p=$i")
                .timeout(10 * 60 * 1000)
                .sslSocketFactory(Application.socketFactory())               .get()
        val images = document.getElementsByTag("IMG")
        print("page  $i")
        for (img in images) {
            val src = img.attr("src")
            val name = src.substring(src.lastIndexOf("/"))
            val file = File("E:/bing$name")
            if (file.exists()){
                file.delete()
            }
            println("E:/bing$name")
            file.createNewFile()
            with(FileOutputStream(file)) {
                write(URL(img.attr("src")).readBytes())
                close()
            }
            sleep(1000)
        }
        sleep(2 * 1000)
    }
}