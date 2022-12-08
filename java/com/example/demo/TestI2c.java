package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wiringop.I2cControl;


public class TestI2c extends Activity {
    private static final String TAG = "TestI2c";
    EditText i2c_port_text;
    EditText i2c_addr_text;
    EditText i2c_reg_text;
    EditText i2c_value_text;

    Button setup_i2c_btn;
    Button read_i2c_btn;
    TextView read_i2c_tv;

    Button write_i2c_btn;
    TextView write_i2c_tv;
    TextView mtestinfo;

    int fd, i2c_addr, reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i2c_main);

        i2c_port_text = (EditText)findViewById(R.id.i2c_port_text);
        i2c_addr_text = (EditText)findViewById(R.id.i2c_addr_text);

        setup_i2c_btn = (Button)findViewById(R.id.setup_i2c_btn);
        read_i2c_btn = (Button)findViewById(R.id.read_i2c_btn);
        read_i2c_tv = (TextView)findViewById(R.id.read_i2c_tv);

        i2c_reg_text = (EditText) findViewById(R.id.i2c_reg_text);
        write_i2c_btn = (Button)findViewById(R.id.write_i2c_btn);
        i2c_value_text = (EditText)findViewById(R.id.i2c_value_text);
        write_i2c_tv = (TextView)findViewById(R.id.write_i2c_tv);
       mtestinfo = (TextView)findViewById(R.id.testinfo);

        read_i2c_btn.setOnClickListener(ocl);
        write_i2c_btn.setOnClickListener(ocl);
        setup_i2c_btn.setOnClickListener(ocl);

    }

    View.OnClickListener ocl = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.setup_i2c_btn:

                    i2c_addr = Integer.valueOf(i2c_addr_text.getText().toString().substring(2),16);
                    Log.i(TAG,"---------addr:" + i2c_addr);


                    fd = I2cControl.wiringPiI2CSetup(i2c_port_text.getText().toString(), i2c_addr);
                    if(fd >= 0)
                        mtestinfo.setText("初始化i2c成功; i2c通道：" + i2c_port_text.getText().toString() + ", 通信地址: 0x" + Integer.toHexString(i2c_addr));

                    break;
                case R.id.read_i2c_btn:
                    if(fd >= 0) {
                        reg = Integer.valueOf(i2c_reg_text.getText().toString().substring(2),16);
                        int value = I2cControl.wiringPiI2CReadReg8(fd, reg);
                        if(value == -1)
                            read_i2c_tv.setText("读取失败");
                        else
                            read_i2c_tv.setText("0x" + Integer.toHexString(value));

                    }

                 //   testinfo.append(fd >= 0 ?"打开成功\n":"打开失败\n");
                    break;
                case R.id.write_i2c_btn:
                    if(fd >= 0) {

                        reg = Integer.valueOf(i2c_reg_text.getText().toString().substring(2),16);
                        int ret = I2cControl.wiringPiI2CWriteReg8(fd, i2c_addr, reg);
                        if(ret < 0)
                            write_i2c_tv.setText("写入失败");
                        else
                            write_i2c_tv.setText("写入成功");
                    }
                    break;
                default:
                    break;
            }

        }
    };

}



