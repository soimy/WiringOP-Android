package com.example.demo;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wiringop.wpiControl;

import java.util.HashMap;
import java.util.Map;
import com.jraska.console.Console;


public class TestGpio extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    Handler handler;
    private String TAG = "TestGpio";
    Map<Integer, CheckBox> idToCb = new HashMap<Integer, CheckBox>();
    Map<Integer, Integer> idToIndex = new HashMap<Integer, Integer>();
    Button r_button;
    int[] physToWpi;
    CharSequence[] physName;
    int pin, pinMax;
    CheckBox cb_gpio;
    Toast toast;
    int i;

    private int[] CHECKBOX_IDS = {
            R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5,
            R.id.cb6, R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10,
            R.id.cb11, R.id.cb12, R.id.cb13, R.id.cb14, R.id.cb15,
            R.id.cb16, R.id.cb17, R.id.cb18, R.id.cb19, R.id.cb20,
            R.id.cb21, R.id.cb22, R.id.cb23, R.id.cb24, R.id.cb25,
            R.id.cb26, R.id.cb27, R.id.cb28, R.id.cb29, R.id.cb30,
            R.id.cb31, R.id.cb32, R.id.cb33, R.id.cb34,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpio_main);
        r_button = findViewById(R.id.bt_readall);
        physToWpi = new int[64];
        physName = new String[64];
        pinMax = wpiControl.getGpioInfo(physToWpi, physName);
        if(pinMax != -1) {
            for(i=pinMax; i<CHECKBOX_IDS.length; i++){
                CheckBox cb = (CheckBox) findViewById(CHECKBOX_IDS[i]);
                cb.setVisibility(View.INVISIBLE);
            }
            for (i = 0; i < pinMax; i++) {
                CheckBox cb = (CheckBox) findViewById(CHECKBOX_IDS[i]);
                cb.setText(physName[i + 1]);
                cb.setOnCheckedChangeListener(this);
                if (physToWpi[i + 1] == -1)
                    cb.setEnabled(false);

                idToIndex.put(CHECKBOX_IDS[i], i);
                idToCb.put(CHECKBOX_IDS[i], cb);
            }
        }
        RootCmd.execRootCmdSilent("chmod 666 /dev/mem");
        wpiControl.wiringPiSetup();
        r_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] str = RootCmd.execRootCmd("gpiox readall");
                Console.clear();
                for(i=0; i<str.length; i++)
                    Console.writeLine(str[i]);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        cb_gpio = idToCb.get(id);
        pin = physToWpi[idToIndex.get(id) + 1];
        wpiControl.pinMode(pin, 1);
        if(cb_gpio.isChecked())
        {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wpiControl.digitalWrite(pin, 1);
                    if(0 == wpiControl.digitalRead(pin))
                    {
                        cb_gpio.setChecked(false);
                        toast=Toast.makeText(getApplicationContext(), "digitalWrite fail", Toast.LENGTH_SHORT);
                    }
                    else {
                        toast = Toast.makeText(getApplicationContext(), "digitalWrite " + pin + " to high", Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            }, 10);
        }
        else {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wpiControl.digitalWrite(pin, 0);
                    if(1 == wpiControl.digitalRead(pin)) {
                        toast=Toast.makeText(getApplicationContext(), "digitalWrite fail", Toast.LENGTH_SHORT);
                    }
                    else {
                            toast = Toast.makeText(getApplicationContext(), "digitalWrite " + pin + " to low", Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
            }, 10);
        }
    }

    public int gpioParse(String gpioStr) {
        if (gpioStr != null && gpioStr.length() == 8) {
            gpioStr = gpioStr.toUpperCase();
            if (gpioStr.charAt(4) >= '0' && gpioStr.charAt(4) <= '8') {
                if (gpioStr.charAt(6) >= 'A' && gpioStr.charAt(6) <= 'D') {
                    return gpioStr.charAt(7) >= '0' && gpioStr.charAt(7) <= '7' ? (gpioStr.charAt(4) - 48) * 32 + (gpioStr.charAt(6) - 65) * 8 + (gpioStr.charAt(7) - 48) : -1;
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        } else {
            System.out.println("input gpio error!");
            return -1;
        }
    }
}
