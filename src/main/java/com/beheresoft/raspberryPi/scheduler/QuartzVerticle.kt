package com.beheresoft.raspberryPi.scheduler

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import org.quartz.*
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.JobBuilder.newJob
import org.quartz.TriggerBuilder.newTrigger
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory

class QuartzVerticle : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        val jobs = config().getJsonArray("quartz")

        val scheduler = StdSchedulerFactory().scheduler
        if (jobs != null && jobs.size() > 0) {
            jobs.forEach { job ->
                job as JsonObject
                val dataStatJobDetail = newJob(DataStatJob::class.java).build()
                val data = dataStatJobDetail.jobDataMap
                data["vert.x"] = vertx
                data["event"] = job.getString("event")
                data["args"] = job.getJsonObject("args")
                val trigger = newTrigger()
                        .withSchedule(cronSchedule(job.getString("cron")))
                        .build()
                scheduler.scheduleJob(dataStatJobDetail, trigger)
            }
        }
        scheduler.start()
        startFuture?.succeeded()
    }

    class DataStatJob : Job {
        private val logger = LoggerFactory.getLogger(DataStatJob::class.java)
        override fun execute(context: JobExecutionContext) {
            val data = context.jobDetail.jobDataMap
            val event = data["event"] as String
            (data["vert.x"] as Vertx).eventBus().send(event, data["args"] ?: JsonObject())
            logger.info("send message to $event ~ ")
        }
    }

}