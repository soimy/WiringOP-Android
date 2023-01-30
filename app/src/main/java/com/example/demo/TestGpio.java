package com.example.demo;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
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
    Button bt_readall;
    private static final int[] CHECKBOX_IDS = {
            R.id.cb1, R.id.cb2, R.id.cb3, R.id.cb4, R.id.cb5,
            R.id.cb6, R.id.cb7, R.id.cb8, R.id.cb9, R.id.cb10,
            R.id.cb11, R.id.cb12, R.id.cb13, R.id.cb14, R.id.cb15,
            R.id.cb16, R.id.cb17, R.id.cb18, R.id.cb19, R.id.cb20,
            R.id.cb21, R.id.cb22, R.id.cb23, R.id.cb24, R.id.cb25,
            R.id.cb26,
    };
    CharSequence physNames[] =
            {
                    "    3.3V", "5V      ",
                    "   SDA.5", "5V      ",
                    "   SCL.5", "GND     ",
                    "   PWM15", "RXD.0   ",
                    "     GND", "TXD.0   ",
                    " GPIO4_B2", "GPIO0_D5 ",
                    " GPIO4_B3", "GND     ",
                    " GPIO0_D4", "GPIO1_D3   ",
                    "    3.3V", "GPIO1_D2   ",
                    "SPI4_TXD", "GND     ",
                    "SPI4_RXD", "GPIO2_D4",
                    "SPI4_CLK", "SPI4_CS1",
                    "     GND", "GPIO1_A3    ",
            };
//    int physToGpio_5[] =
//            {
//                    -1,       // 0
//                    -1, -1,   // 1, 2
//                    47, -1,   // 3, 4
//                    46, -1,   // 5, 6
//                    54, 131,   // 7, 8
//                    -1, 132,   // 9, 10
//                    138, 29,   // 11, 12
//                    139, -1,   // 13, 14
//                    28, 59,   // 15, 16
//                    -1, 58,   // 17, 18
//                    49, -1,   // 19, 20
//                    48, 92,   // 21, 22
//                    50, 52,   // 23, 24
//                    -1, 35,   // 25, 26
//            };
    int physToGpio_5[] =
            {
                    -1,       // 0
                    -1, -1,   // 1, 2
                    -1, -1,   // 3, 4
                    -1, -1,   // 5, 6
                    -1, -1,   // 7, 8
                    -1, -1,   // 9, 10
                    138, 29,   // 11, 12
                    139, -1,   // 13, 14
                    28, 59,   // 15, 16
                    -1, 58,   // 17, 18
                    -1, -1,   // 19, 20
                    -1, 92,   // 21, 22
                    -1, -1,   // 23, 24
                    -1, 35,   // 25, 26
            };
    int physToPin_5[] =
            {
                    -1,       // 0
                    -1, -1,   // 1, 2
                    0, -1,   // 3, 4
                    46, -1,   // 5, 6
                    2, 3,   // 7, 8
                    -1, 4,   // 9, 10
                    5, 6,   // 11, 12
                    7, -1,   // 13, 14
                    8, 9,   // 15, 16
                    -1, 10,   // 17, 18
                    11, -1,   // 19, 20
                    12, 13,   // 21, 22
                    14, 15,   // 23, 24
                    -1, 16,   // 25, 26
            };
             int pinToGpio_5[] =
            {
                    47, 46,      // 0, 1
                    54, 131,      // 2, 3
                    132, 138,      // 4  5
                    29, 139,      // 6, 7
                    28, 59,      // 8, 9
                    58, 49,      //10,11
                    48, 92,      //12,13
                    50, 52,      //14,15
                    35,
            };

    int pin;
    CheckBox cb_gpio;
    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpio_main);
        bt_readall = findViewById(R.id.bt_readall);
        bt_readall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] str = RootCmd.execRootCmd("gpiox readall");
                Console.clear();
                for(int i=0; i<str.length; i++)
                    Console.writeLine(str[i]);
            }
        });
        for (int i = 0; i < CHECKBOX_IDS.length; i++) {
            CheckBox cb = (CheckBox) findViewById(CHECKBOX_IDS[i]);
            cb.setText(physNames[i]);
            cb.setOnCheckedChangeListener(this);
            if (physToGpio_5[i + 1] == -1)
                cb.setEnabled(false);

            idToIndex.put(CHECKBOX_IDS[i], i);
            idToCb.put(CHECKBOX_IDS[i], cb);
        }
        RootCmd.execRootCmdSilent("chmod 666 /dev/mem");
        wpiControl.wiringPiSetup();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        cb_gpio = idToCb.get(id);
        pin = physToPin_5[idToIndex.get(id) + 1];
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
