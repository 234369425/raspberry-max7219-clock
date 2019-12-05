package com.beheresoft.raspberryPi.verticle

import com.beheresoft.raspberryPi.DISPLAY_ADDRESS
import com.beheresoft.raspberryPi.plugin.Weather
import com.beheresoft.raspberryPi.verticle.bean.DisplayMessage
import com.beheresoft.raspberryPi.verticle.bean.DisplayMessageCodec
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject

class WeatherVerticle : AbstractVerticle() {

    override fun start() {
        vertx.eventBus().consumer<JsonObject>("quartz:read:weather")
                .handler {
                    val args = it.body()
                    Weather.get(args.getString("filePath"))
                    vertx.eventBus().send(DISPLAY_ADDRESS,
                        DisplayMessage(Weather.temp, level = DisplayMessage.LEVEL.HIGH, millis = 4 * 1000)
                    )
                }
    }
}