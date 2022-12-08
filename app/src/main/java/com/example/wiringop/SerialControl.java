package com.example.wiringop;

public class SerialControl {

    static {
        System.loadLibrary("WiringOP");
    }

    public final static native int serialOpen(String dev, int baud);
    public final static native void serialClose(int fd);
    public final static native void serialFlush(int fd);
    public final static native void serialPutchar(int fd, char c);
    public final static native void serialPuts(int fd, String s) ;
    public final static native int serialDataAvail(int fd);
    public final static native int serialGetchar(int fd);
}