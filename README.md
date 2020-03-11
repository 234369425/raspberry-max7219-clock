
一个树莓派8x8 16块LED组成的电子时钟

## 基于pi4j开发，如果使用需要首先安装pi4j ##

安装pi4j
    curl -sSL https://pi4j.com/install | sudo bash

下载PI4J：
[https://pi4j.com/download/pi4j-1.2.zip](https://pi4j.com/download/pi4j-1.2.zip "下载pi4j")

我用的是3b+ 参考SPI接口图（也可以使用gpio readall查看）：
[https://pi4j.com/1.2/pins/model-3b-plus-rev1.html](https://pi4j.com/1.2/pins/model-3b-plus-rev1.html "PI4J")



- 可以用树莓派raspi-config开启SPI
- 或者修改sudo vi /boot/config.txt 修改 dtparam=spi=on reboot

project config
main class = io.vertx.core.Launcher
Program arguments = run com.beheresoft.raspberryPi.MainVerticle
                    -conf src/main/resources/config.json
                    -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory
                    

树莓派java调用https的时候，有SSL问题，所以直接用curl下载完按照文件处理
天气预报：
使用nowapi的接口



假装自己是大本钟
想要调用play播放声音
sudo apt-get install sox
sudo apt-get install sox libsox-fmt-all
将resource都移动到 /opt/java/

声音播放所需类库

    sudo apt-get install sox
	sudo apt-get install sox libsox-fmt-all


