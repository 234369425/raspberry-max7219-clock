package com.beheresoft.raspberryPi.verticle.bean

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import io.vertx.core.json.JsonObject

class DisplayMessageCodec : MessageCodec<DisplayMessage, DisplayMessage> {
    override fun decodeFromWire(pos: Int, buffer: Buffer): DisplayMessage {
        var ps = pos
        var length = buffer.getInt(ps)
        ps += 4
        val start = ps
        ps += length
        val end = ps
        val jsonString = buffer.getString(start, end)
        val json = JsonObject(jsonString)
        return DisplayMessage(json.getString("message"), json.getLong("millis"),
                DisplayMessage.LEVEL.valueOf(json.getString("level")), json.getInteger("brightness").toByte())
    }

    override fun systemCodecID(): Byte {
        return -1
    }

    override fun encodeToWire(buffer: Buffer, s: DisplayMessage) {
        val jsonObject = JsonObject()
        jsonObject.put("brightness", s.brightness)
        jsonObject.put("level", s.level)
        jsonObject.put("message", s.message)
        jsonObject.put("millis", s.millis)
        val str = jsonObject.encode()
        buffer.appendInt(str.length)
        buffer.appendString(str)
    }

    override fun transform(s: DisplayMessage): DisplayMessage {
        return s
    }

    override fun name(): String {
        return DisplayMessageCodec::class.java.simpleName
    }

}