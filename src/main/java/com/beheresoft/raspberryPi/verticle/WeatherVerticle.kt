package com.beheresoft.raspberryPi.verticle

import com.beheresoft.raspberryPi.plugin.Weather
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import org.slf4j.LoggerFactory

class WeatherVerticle : AbstractVerticle() {

    val logger = LoggerFactory.getLogger(WeatherVerticle::class.java)

    override fun start(startFuture: Future<Void>?) {
        vertx.eventBus().consumer<JsonObject>("quartz:read:weather")
                .handler {
                    val args = it.body()
                    Weather.get(args.getString("filePath"))
                }
    }
}