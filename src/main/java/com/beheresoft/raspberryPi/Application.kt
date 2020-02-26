package com.beheresoft.raspberryPi

import com.beheresoft.big.font.LedBitmap16_16
import com.beheresoft.big.font.MyStrings
import com.beheresoft.raspberryPi.font.FontModel
import com.beheresoft.raspberryPi.io.max7219.BitOperation.printByConsole
import com.beheresoft.raspberryPi.io.max7219.Controller
import io.vertx.core.json.JsonObject
import java.security.KeyManagementException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object Application {

    private var debug = false
    //var fontFile = "i:/clock/fonts/hzk16f"
    var fontFile = "d:/Microsoft YaHei UI Light_cpr_16_16.bft"

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

    fun socketFactory(): SSLSocketFactory {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            return sslContext.socketFactory
        } catch (e: Exception) {
            when (e) {
                is RuntimeException, is KeyManagementException -> {
                    throw RuntimeException("Failed to create a SSL socket factory", e)
                }
                else -> throw e
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("debug", "true")
        System.setProperty("font.location", "I:\\clock\\fonts")

        val string = "哈AA"
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