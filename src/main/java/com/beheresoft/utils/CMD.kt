package com.beheresoft.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

object CMD {

    fun exec(vararg cmd: String, timeout: Long = -1, f: ((String) -> Unit)): Process {
        val builder = ProcessBuilder(*cmd)
        builder.redirectErrorStream(true)
        val process = builder.start()
        thread {
            process.inputStream.use { ips ->
                BufferedReader(InputStreamReader(ips)).use { buffer ->
                    var res: String
                    while (true) {
                        res = buffer.readLine() ?: break
                        f(res)
                    }
                }
            }
        }
        if (timeout > 0) {
            process.waitFor(timeout, TimeUnit.MILLISECONDS)
        } else {
            process.waitFor()
        }
        return process
    }


}