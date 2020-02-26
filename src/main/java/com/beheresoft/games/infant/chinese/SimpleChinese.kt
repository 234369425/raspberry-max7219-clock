package com.beheresoft.games.infant.chinese

import com.beheresoft.raspberryPi.Application.socketFactory
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URLEncoder
import java.security.KeyManagementException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.net.URL


object SimpleChinese {

    fun getMp3(string: String) {
        val document = Jsoup.connect("https://dict.baidu.com/s?wd=" + URLEncoder.encode(string, "UTF-8"))
                .timeout(60 * 1000)
                .sslSocketFactory(socketFactory())
                .get()
        val elements = document.getElementById("pinyin").getElementsByTag("A")
        var element: Element? = null
        if (elements.size > 0) {
            element = elements[0]
        }
        val url = element?.attributes()?.getIgnoreCase("url")
        if (url != null) {
            playMp3(url)
        }
    }

    fun playMp3(url: String) {
        /**
        val inMp3 = AudioFormat(AudioFormat.MPEGLAYER3)
        val outLinear = AudioFormat(AudioFormat.LINEAR)
        PlugInManager.addPlugIn(
                "com.sun.media.codec.audio.mp3.JavaDecoder",
                arrayOf<Format>(inMp3), arrayOf<Format>(outLinear),
                CODEC)
        val player = Manager.createPlayer(URL(url))
        player.start()*/
    }


    @JvmStatic
    fun main(args: Array<String>) {
        SimpleChinese.getMp3("了")
    }

}