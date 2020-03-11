## 一个树莓派8x8Max7219 上下两排16块LED组成的电子时钟，包含日期展现和天气预报展现，支持中文 ##

![](https://raw.githubusercontent.com/234369425/raspberry-max7219-clock/master/images/clock.jpg)

我用的是3b+ 参考SPI接口图（也可以使用gpio readall查看）：
[https://pi4j.com/1.2/pins/model-3b-plus-rev1.html](https://pi4j.com/1.2/pins/model-3b-plus-rev1.html "PI4J")

接线图
![](https://raw.githubusercontent.com/234369425/raspberry-max7219-clock/master/images/lines_rasp_1.jpg "Max7219")

![](https://raw.githubusercontent.com/234369425/raspberry-max7219-clock/master/images/lines_rasp_2.jpg "raspberry")


## 基于pi4j spi驱动开发，如果使用需要首先安装pi4j ##

安装pi4j
    curl -sSL https://pi4j.com/install | sudo bash

## 开启树莓派SPI ##

- 可以用树莓派raspi-config开启SPI
- 或者修改sudo vi /boot/config.txt 修改 dtparam=spi=on reboot

## 基于vertx 在intellij 项目启动配置：##
    project config
    main class = io.vertx.core.Launcher
    Program arguments = run com.beheresoft.raspberryPi.MainVerticle
    -conf src/main/resources/config.json
    -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory
    

树莓派java调用https的时候，有SSL问题，所以直接用curl下载完按照文件处理

需要设置的定时任务在sells内

天气预报：
	使用nowapi的接口,需要去now api申请key
	https://www.nowapi.com/


假装自己是大本钟,需要调用play播放声音，并且使用3.5mm接口输出声音
声音播放所需类库：

    sudo apt-get install sox
	sudo apt-get install sox libsox-fmt-all


