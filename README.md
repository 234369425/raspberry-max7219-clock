project config
main class = io.vertx.core.Launcher
Program arguments = run com.beheresoft.raspberryPi.MainVerticle
                    -conf src/main/resources/config.json
                    -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory
                    

树莓派java调用https的时候，有SSL问题，所以直接用curl下载完按照文件处理
