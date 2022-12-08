package com.example.wiringop;

public class I2cControl {

    static {
        System.loadLibrary("WiringOP");
    }

    public final static native int wiringPiI2CRead (int fd);
    public final static native int wiringPiI2CReadReg8 (int fd, int reg);
    public final static native int wiringPiI2CReadReg16 (int fd, int reg);

    public final static native int wiringPiI2CWrite (int fd, int data);
    public final static native int wiringPiI2CWriteReg8 (int fd, int reg, int value);
    public final static native int wiringPiI2CWriteReg16 (int fd, int reg, int value);

    public final static native int wiringPiI2CSetup (String device, int devId);
}