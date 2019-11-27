package com.beheresoft.raspberryPi.io.max7219;

public class BitOperation {

    /**
     * kotlin 位运算很尴尬，补充方法
     * @param s
     * @param i
     * @return
     */
    public static int shortAndInt(short s , int i){
        return s & i;
    }

    public static void printByConsole(short[] buffer) {
        int key[] =
                {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

        int strLength = buffer.length;
        if (strLength % 32 != 0) {
            System.out.println("字符串缓存返回错误（不是32的倍数）");
            return;
        }
        for (int len = 0; len < strLength / 32; len++) {
            for (int k = 0; k < 16; k++) {
                for (int j = 0; j < 2; j++) {
                    short byteDate = buffer[k * 2 + j + len * 32];
                    for (int i = 0; i < 8; i++) {
                        int flag = byteDate & key[i];
                        System.out.print(flag == 0 ? "　" : "●");
                    }
                }
                System.out.println();
            }
            System.out.println("************************");
        }
    }

    public static void printByConsole(byte[] buffer) {
        int key[] =
                {0x80, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

        int strLength = buffer.length;
        if (strLength % 32 != 0) {
            System.out.println("字符串缓存返回错误（不是32的倍数）");
            return;
        }
        for (int len = 0; len < strLength / 32; len++) {
            for (int k = 0; k < 16; k++) {
                for (int j = 0; j < 2; j++) {
                    short byteDate = buffer[k * 2 + j + len * 32];
                    for (int i = 0; i < 8; i++) {
                        int flag = byteDate & key[i];
                        System.out.print(flag == 0 ? "　" : "●");
                    }
                }
                System.out.println();
            }
            System.out.println("************************");
        }
    }

}
