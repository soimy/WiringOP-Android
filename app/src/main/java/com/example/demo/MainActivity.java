package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wiringop.GPIOControl;

public class MainActivity extends Activity {

    private String TAG = "MainActivity-";
    final int gpioPin = 359;
    final int[] retValue = {-1};
    final int[] fd = new int[1];
    final int[] c = new int[1];
    final int[] ret = new int[1];
    final String data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button gpio_test_btn = (Button) findViewById(R.id.gpio_test_btn);
        Button serial_test_btn = (Button) findViewById(R.id.serial_test_btn);
        Button i2c_test_btn = (Button) findViewById(R.id.i2c_test_btn);
        Button spi_test_btn = (Button) findViewById(R.id.spi_test_btn);

        gpio_test_btn.setOnClickListener(ocl);
        serial_test_btn.setOnClickListener(ocl);
        i2c_test_btn.setOnClickListener(ocl);
        spi_test_btn.setOnClickListener(ocl);

        /******************************GPIO TEST****************************************/

        /*
        GPIOControl.doExport(33);

        new Thread() {
            @Override
            public void run() {
                super.run();
                GPIOControl.doExport(33);
                while(true){
                    try {
                        Thread.sleep(1000);//休眠1秒
                        GPIOControl.pinMode(33, 1); //33对应GPIO1_A1
                        GPIOControl.digitalWrite(33, 1);
                        Thread.sleep(1000);//休眠1秒
                        GPIOControl.pinMode(33, 1);
                        GPIOControl.digitalWrite(33, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }.start();*/

        /******************************UART TEST****************************************/
        /*
        new Thread() {
            @Override
            public void run() {
                super.run();
                fd[0] = SerialControl.serialOpen("/dev/ttyS4", 115200);
                SerialControl.serialPutchar(fd[0], 'a') ;
                c[0] = SerialControl.serialGetchar(fd[0]);
                Log.i(TAG,"-----c[0]:\t" + c[0]);
            }
        }.start();*/

        /******************************I2c TEST****************************************/
/*
        new Thread() {
            @Override
            public void run() {
                super.run();
                try{
                    fd[0] = I2cControl.wiringPiI2CSetup("/dev/i2c-2", 0x38);
                    Log.i(TAG,"-----f[0]:\t" + fd[0]);

                    c[0] = I2cControl.wiringPiI2CReadReg8(fd[0], 1);
                    Log.i(TAG,"-----c[0]:\t" + c[0]);

                    while(1 < 100) {
                        Thread.sleep(500);
                        c[0] = I2cControl.wiringPiI2CWrite(fd[0], 0x80);

                        Thread.sleep(500);
                        c[0] = I2cControl.wiringPiI2CWrite(fd[0], 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();*/

        /******************************SPI TEST****************************************/
/*
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    fd[0] = SpiControl.wiringPiSPISetup(0, 2000000);
                    byte[] data = {9, 9, 9, 9};
                    int[] rc = new int[1];
                    data[0] = (byte) 0x9f;
                    while (true) {

                        rc[0] = SpiControl.wiringPiSPIDataRW(0, data, 4);

                        Log.i(TAG, "----lee-ret[0]:\t" + rc[0]);
                        Log.i(TAG, "----lee-data[0]:\t" + data[1]);
                        Log.i(TAG, "----lee-data[0]:\t" + data[2]);
                        Log.i(TAG, "----lee-data[0]:\t" + data[3]);

                        //          SpiControl.testSpi(0);

                        Thread.sleep(5000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();*/

    }
    View.OnClickListener ocl =new View.OnClickListener() {
        Intent intent;
        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.gpio_test_btn:
                    intent = new Intent(MainActivity.this, TestGpio.class);
                    startActivity(intent);
                    break;
                case R.id.serial_test_btn:
                    intent = new Intent(MainActivity.this, TestSerialPort.class);
                    startActivity(intent);
                    break;
                case R.id.i2c_test_btn:
                    intent = new Intent(MainActivity.this, TestI2c.class);
                    startActivity(intent);
                    break;
                case R.id.spi_test_btn:
                    intent = new Intent(MainActivity.this, TestSpi.class);
                    startActivity(intent);
                    break;
            }
        }
    };

}
