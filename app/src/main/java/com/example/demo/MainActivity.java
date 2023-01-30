package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import com.example.wiringop.wpiControl;

public class MainActivity extends AppCompatActivity {

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
        Button pwm_test_btn = (Button) findViewById(R.id.pwm_test_btn);

        gpio_test_btn.setOnClickListener(ocl);
        serial_test_btn.setOnClickListener(ocl);
        i2c_test_btn.setOnClickListener(ocl);
        spi_test_btn.setOnClickListener(ocl);
        pwm_test_btn.setOnClickListener(ocl);

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
                case R.id.pwm_test_btn:
                    intent = new Intent(MainActivity.this, TestPwm.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
