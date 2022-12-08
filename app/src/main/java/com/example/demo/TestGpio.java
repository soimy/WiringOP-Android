package com.example.demo;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wiringop.GPIOControl;

public class TestGpio extends Activity{

    Button gpio_num_btn;
    TextView gpio_num_tv;

    Button read_gpio_btn;
    TextView read_gpio_tv;

    Button write_gpio_1_btn,write_gpio_0_btn;
    TextView write_gpio_tv;

    EditText gpio_text;

    Handler handler;

    //GPIO8_A7
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpio_main);

        gpio_text = (EditText)findViewById(R.id.gpio_text);

        gpio_num_btn = (Button)findViewById(R.id.gpio_num_btn);
        gpio_num_tv = (TextView)findViewById(R.id.gpio_num_tv);

        read_gpio_btn = (Button)findViewById(R.id.read_gpio_btn);
        read_gpio_tv = (TextView)findViewById(R.id.read_gpio_tv);

        write_gpio_0_btn = (Button)findViewById(R.id.write_gpio_0_btn);
        write_gpio_1_btn = (Button)findViewById(R.id.write_gpio_1_btn);
        write_gpio_tv = (TextView)findViewById(R.id.write_gpio_tv);


        gpio_num_btn.setOnClickListener(ocl);
        read_gpio_btn.setOnClickListener(ocl);
        write_gpio_0_btn.setOnClickListener(ocl);
        write_gpio_1_btn.setOnClickListener(ocl);

    }

    OnClickListener ocl =new OnClickListener() {

        int pin;
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub

            switch (arg0.getId()) {
                case R.id.gpio_num_btn:
                    gpio_num_tv.setText(gpioParse(gpio_text.getText().toString())+"");
                    break;
                case R.id.read_gpio_btn:
                    pin = gpioParse(gpio_text.getText().toString());
                    GPIOControl.doExport(pin);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GPIOControl.pinMode(pin, 0);
                            int value = GPIOControl.digitalRead(pin);

                            read_gpio_tv.setText(value+"");
                        }
                    }, 10);

                    break;
                case R.id.write_gpio_0_btn:
                    pin = gpioParse(gpio_text.getText().toString());
                    GPIOControl.doExport(pin);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GPIOControl.pinMode(pin, 1);
                            write_gpio_tv.setText(GPIOControl.digitalWrite(pin, 0) == 0 ? "写入失败" : "写入成功");
                            GPIOControl.doUnexport(pin);
                        }
                    }, 10);

                    break;
                case R.id.write_gpio_1_btn:
                    pin = gpioParse(gpio_text.getText().toString());
                    GPIOControl.doExport(pin);
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GPIOControl.pinMode(pin, 1);
                            write_gpio_tv.setText(GPIOControl.digitalWrite(pin, 1) == 0 ? "写入失败" : "写入成功");
                            GPIOControl.doUnexport(pin);
                        }
                    }, 10);

                    break;
                default:
                    break;
            }
        }
    };

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