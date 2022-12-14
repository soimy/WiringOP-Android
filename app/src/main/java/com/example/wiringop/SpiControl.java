package com.example.wiringop;

public class SpiControl {

    static {
        System.loadLibrary("WiringOP");
    }

    public final static native int wiringPiSPIGetFd (int channel);
    public final static native int wiringPiSPIDataRW (int channel, byte[] data, int len);
    public final static native int wiringPiSPISetupMode (int channel, int port, int speed, int mode);
    public final static native int wiringPiSPISetup (int channel, int speed);
    //public final static native int testSpi(int channel);
}