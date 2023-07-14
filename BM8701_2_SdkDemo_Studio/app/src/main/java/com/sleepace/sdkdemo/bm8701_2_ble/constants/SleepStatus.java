package com.sleepace.sdkdemo.bm8701_2_ble.constants;

/**
 * 实时状态
 */
public final class SleepStatus {
    /**
     * 0xFF:初始化
     */
    public static final byte INIT = (byte) 0xff;
    /**
     * 0x00:离床
     */
    public static final byte OUT_OF_BED = 0x00;
    /**
     * 0x31:坐起
     */
    public static final byte SIT_UP = 0x31;
    /**
     * 0x11:呼吸暂停
     */
    public static final byte APNEA = 0x11;
    /**
     * 0x23:翻身
     */
    public static final byte TURN_OVER = 0x23;
    /**
     * 0x22:体动
     */
    public static final byte BODY_MOVE = 0x22;
    /**
     * 0x01:在床
     */
    public static final byte ON_BED = 0x01;
}
