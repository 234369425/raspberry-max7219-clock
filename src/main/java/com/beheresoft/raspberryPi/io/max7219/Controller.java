package com.beheresoft.raspberryPi.io.max7219;

import com.beheresoft.big.font.LedBitmap16_16;
import com.beheresoft.raspberryPi.Application;
import com.beheresoft.raspberryPi.font.FontModel;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;

public class Controller {

    private SPEED scrollSpeed = SPEED.NORMAL;
    private SpiDevice spi;
    private DIRECTION direction = DIRECTION.EAST;
    private final int rows = 2;
    private final Object[] buffers;
    private final int bytesEachBoard;
    private final int boardCount;
    private final int boardRowCount;

    public Controller() {
        this(16, 2, 8);
    }

    public Controller(int boardCount, int rows, int bytesEachBoard) {
        this.boardCount = boardCount;
        this.buffers = new Object[rows];
        this.bytesEachBoard = bytesEachBoard;
        this.boardRowCount = boardCount / rows;
        for (int i = 0; i < rows; i++) {
            buffers[i] = new byte[bytesEachBoard * boardCount / rows];
        }

        if (!Application.INSTANCE.debug()) {
            try {
                this.spi = SpiFactory.getInstance(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED,
                        SpiDevice.DEFAULT_SPI_MODE);

                command(LedBitmap16_16.Constants.MAX7219_REG_SCANLIMIT, (byte) 0x7);
                command(LedBitmap16_16.Constants.MAX7219_REG_DECODEMODE, (byte) 0x0);
                command(LedBitmap16_16.Constants.MAX7219_REG_DISPLAYTEST, (byte) 0x0);
                this.brightnessLevel((byte) 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showRightNow16(String text) {
        short[] values = FontModel.INSTANCE.parse(text);
        byte[] data = rotate(splitDataIntoRows(values));
        int[] index = new int[buffers.length];
        int bufferIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (i % 32 == 0) {
                if (++bufferIndex >= buffers.length) {
                    bufferIndex = 0;
                }
            }
            byte[] buffer = (byte[]) buffers[bufferIndex];
            if (index[buffers.length - 1] >= buffer.length) {
                break;
            }
            buffer[index[bufferIndex]++] = data[i];
        }
        this.flush();
    }

    /*
     * 将数据分割到行内
     * 暂时只实现2行
     * @return
     */
    public byte[] splitDataIntoRows(short[] data) {
        //8 + 8 + 8 + 8 点阵，需要长度32凑成一个字
        if (data.length % 32 != 0) {
            return null;
        }
        byte[] res = new byte[data.length];

        for (int i = 0; i < data.length / 32; i++) {
            for (int j = 0; j < 32; j++) {
                if (j / bytesEachBoard % 2 == 0) {
                    System.out.println((j + i * 32) + "---" + (j * 2 + i * 32));
                    res[j + i * 32] = (byte) (0xff & data[j + i * 32]);
                } else {
                    System.out.println((j + i * 32) + "---" + (j * 2 - 15 + i * 32));
                    res[j + i * 32] = (byte) (0xff & data[j - 1 + i * 32]);
                }
            }
        }
        return res;
    }

    public void clear() {
        for (int i = 0; i < this.boardCount; i++) {
            for (short j = 0; j < bytesEachBoard; j++) {
                setByte(i, bytesEachBoard, (byte) 0x00);
            }
        }
        this.flush();
    }

    private void flush() {
        Object[] willDraw = new Object[buffers.length];
        if (direction != DIRECTION.NORTH) {
            for (int i = 0; i < willDraw.length; i++) {
                willDraw[0] = rotate((byte[]) buffers[i]);
            }
        }
        for (int position = 0; position < bytesEachBoard; position++) {
            int len = 2 * this.boardCount;
            byte[] ret = new byte[len];
            for (int i = 0; i < this.boardCount; i++) {
                byte[] buffer = (byte[]) buffers[i % rows];
                ret[2 * i] = (byte) ((position + REG_DIGIT0) & 0xff);
                ret[2 * i + 1] = buffer[i % buffers.length + position];
            }
            spiWrite(ret);
        }
    }

    private byte[] rotate(byte[] data) {
        byte[] result = new byte[data.length];

        for (int i = 0; i < this.boardCount * bytesEachBoard / 2; i += bytesEachBoard) {
            byte[] tile = new byte[bytesEachBoard];
            for (int j = 0; j < bytesEachBoard; j++) {
                tile[j] = data[i + j];
            }
            int k = direction.angle / 90;
            for (int j = 0; j < k; j++) {
                tile = this.rotateOneBlock(tile);
            }
            for (int j = 0; j < bytesEachBoard; j++) {
                result[i + j] = tile[j];
            }
        }
        return result;
    }

    private byte[] rotateOneBlock(byte[] buf) {
        byte[] result = new byte[8];
        for (int i = 0; i < 8; i++) {
            short b = 0;
            short t = (short) ((0x01 << i) & 0xff);
            for (int j = 0; j < 8; j++) {
                int d = 7 - i - j;
                if (d > 0)
                    b += (short) ((buf[j] & t) << d);
                else
                    b += (short) ((buf[j] & t) >> (-1 * d));
            }
            result[i] = (byte) b;
        }
        return result;
    }

    private void setByte(int board, int byteIndex, byte value) {
        int index = board / rows;
        byte[] buffer = (byte[]) buffers[board / rows];
        buffer[index * bytesEachBoard + byteIndex] = value;
    }

    public void brightnessLevel(byte level) {
        level = level < 0 ? 0 : (level > 15 ? 15 : level);
        this.command(REG_INTENSITY, level);
    }

    /**
     * 发送指令 data 到寄存器 register
     */
    private void command(byte register, byte data) {
        int len = 2 * this.boardCount;
        byte[] buf = new byte[len];
        for (int i = 0; i < len; i += 2) {
            buf[i] = register;
            buf[i + 1] = data;
        }
        this.spiWrite(buf);
    }

    private void spiWrite(byte[] bytes) {
        Application.INSTANCE.printBytes(bytes);
        if (!Application.INSTANCE.debug()) {
            try {
                this.spi.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public enum DIRECTION {
        NORTH(0), SOUTH(180), WEST(270), EAST(90);

        DIRECTION(int angle) {
            this.angle = angle;
        }

        int angle = 0;

        public int getAngle() {
            return angle;
        }

    }

    public enum SPEED {
        SLOW(100), NORMAL(50), FAST(10);
        int speed = 0;

        SPEED(int speed) {
            this.speed = speed;
        }

        public int getSpeed() {
            return speed;
        }

    }


    private static byte REG_NOOP = 0x0;
    private static byte REG_DIGIT0 = 0x1;
    private static byte REG_DIGIT1 = 0x2;
    private static byte REG_DIGIT2 = 0x3;
    private static byte REG_DIGIT3 = 0x4;
    private static byte REG_DIGIT4 = 0x5;
    private static byte REG_DIGIT5 = 0x6;
    private static byte REG_DIGIT6 = 0x7;
    private static byte REG_DIGIT7 = 0x8;
    private static byte REG_DECODEMODE = 0x9;
    private static byte REG_INTENSITY = 0xA;
    private static byte REG_SCANLIMIT = 0xB;
    private static byte REG_SHUTDOWN = 0xC;
    private static byte REG_DISPLAYTEST = 0xF;
}
