package com.example.wiringop;

public class wpiControl {

    static {
        System.loadLibrary("WiringOP");
    }
    public final static native int wiringPiSetup();
    public final static native void pinMode(int pin, int mode);
    public final static native void pullUpDnControl(int pin, int pud) ;
    public final static native int digitalRead(int pin);
    public final static native void digitalWrite(int pin, int value);

    public final static native int wiringPiSPIGetFd (int channel);
    public final static native int wiringPiSPIDataRW (int channel, byte[] data, int len);
    public final static native int wiringPiSPISetupMode (int channel, int port, int speed, int mode);
    public final static native int wiringPiSPISetup (int channel, int speed);

    public final static native int serialOpen(String dev, int baud);
    public final static native void serialClose(int fd);
    public final static native void serialFlush(int fd);
    public final static native void serialPutchar(int fd, byte c);
    public final static native void serialPuts(int fd, String s) ;
    public final static native int serialDataAvail(int fd);
    public final static native int serialGetchar(int fd);

    public final static native int wiringPiI2CRead (int fd);
    public final static native int wiringPiI2CReadReg8 (int fd, int reg);
    public final static native int wiringPiI2CReadReg16 (int fd, int reg);
    public final static native int wiringPiI2CWrite (int fd, int data);
    public final static native int wiringPiI2CWriteReg8 (int fd, int reg, int value);
    public final static native int wiringPiI2CWriteReg16 (int fd, int reg, int value);
    public final static native int wiringPiI2CSetup (int devId);
    public final static native int wiringPiI2CSetupInterface  (String device, int devId);
}