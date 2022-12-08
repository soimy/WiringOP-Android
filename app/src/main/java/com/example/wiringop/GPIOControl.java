package com.example.wiringop;

public class GPIOControl {
    static {
        System.loadLibrary("WiringOP");
    }

    public final static native int doExport(int pin);

    public final static native int pinMode(int pin, int mode);

    public final static native int digitalRead(int pin);

    public final static native int digitalWrite(int pin, int value);

    public final static native int doUnexport(int pin);
}