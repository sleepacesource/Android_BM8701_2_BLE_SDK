package com.sleepace.sdkdemo.bm8701_2_ble.constants;

/**
 * 实时睡眠状态
 */
public final class SleepState {
    /**
     * 0xFF:初始化
     */
    public static final byte INIT = (byte) 0xff;
    /**
     * 0xE0:深睡
     */
    public static final byte DEEP = (byte) 0xE0;
    /**
     * 0xE2:浅睡
     */
    public static final byte LIGHT = (byte) 0xE2;
    /**
     * 0xE3:清醒
     */
    public static final byte WAKE = (byte) 0xE3;
}
