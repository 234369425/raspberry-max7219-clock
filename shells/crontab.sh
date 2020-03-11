*/1 * * * * curl "https://api.seniverse.com/v3/weather/now.json?key=youKey&language=zh-Hans" > /opt/java/data/weather.json
45 8 * * * play -v 0.5 /opt/java/sounds/jay_qhc.mp3
0 8-11 * * * play /opt/java/sounds/clock_hours.mp3
0 13-22 * * * play /opt/java/sounds/clock_hours.mp3
0 12 * * * play /opt/java/sounds/clock_12.mp3
25 7 * * * play -v 0.2 /opt/java/sounds/ybn_stb.wav
0 */2 * * * sh /opt/java/start
