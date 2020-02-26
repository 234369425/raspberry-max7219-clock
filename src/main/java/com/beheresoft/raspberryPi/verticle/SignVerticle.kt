package com.beheresoft.raspberryPi.verticle

import com.beheresoft.raspberryPi.Application
import io.vertx.core.AbstractVerticle
import org.jsoup.Connection
import org.jsoup.Jsoup

class SignVerticle : AbstractVerticle() {

    override fun start() {
        val cookie = "xAgentAID=19011f0b-16f8063cc5fa-6dceccf27efc406f84bc3a40010c97ba; xAgentUID=XpAcgM5oLDN4OUHrJfm8fSfbCDRiYXAAlNxblsSmZ74%3D"
        val userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.9(0x17000929) NetType/WIFI Language/zh_CN"
        val res = Jsoup.connect("https://weclub.ccc.cmbchina.com/SCRMCustomActivityFront/checkin/home?activityCode=checkin&timestamp=1578410389874&nickname=%2FV9ZmfBQBb%2BdLVqtBQSl3w%3D%3D&state=KqG4VdWp2l7FzmlttMGDgg%3D%3D&unionId=So0N75%2BpOTBriKBH275iMdfrAHqGiGDLBhEFApk%2F76w%3D&openid=XpAcgM5oLDN4OUHrJfm8fSfbCDRiYXAAlNxblsSmZ74%3D&headimgurl=02o%2BgBgmk1NGggtN67uFXw7v%2Fksb3l0Ezf2uFHdK2GavsGRtTaVCWvSSO1tE1FvIhM3cvLiJqQnl1%2BVzapw5k3QZoqnvPyLRImEvsJ4lac6f2MaPua05D6lKkEBsumtoQVWzGuZuZ8IpgQAa7gr%2BLtXe9N12nI5ocyMj7lADL8LvwXo7bb9rlXSRWaBVvZey&channel=WeiXin&signature=346a725ffa")
                .header("Cookie",cookie)
                .header("UserAgent",userAgent)
                .sslSocketFactory(Application.socketFactory())
                .method(Connection.Method.GET)
                .execute()
        println(res.body())
        val document = Jsoup.connect("https://weclub.ccc.cmbchina.com/SCRMCustomActivityFront/checkin/request/checkin.json")
                .header("UserAgent", userAgent)
                .header("Cookie", "xAgentAID=19011f0b-16f8063cc5fa-6dceccf27efc406f84bc3a40010c97ba; xAgentUID=XpAcgM5oLDN4OUHrJfm8fSfbCDRiYXAAlNxblsSmZ74%3D; accessToken=e443f1958235dee592f5568c394ba5802dc9380d87d0fc5ea1a35716c26e3dd8")
                .ignoreContentType(true)
                .requestBody("{\"activityCode\":\"checkin\"}")
                .timeout(60 * 1000)
                .sslSocketFactory(Application.socketFactory())
                .post()
        "".contains("/")
        "".startsWith("",true)
        println(document)
    }


}

fun main(args: Array<String>) {
    SignVerticle().start()
}