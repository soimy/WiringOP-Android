package com.example.demo;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.wiringop.SerialControl;

public class TestSerialPort extends Activity{
    private static final String TAG = "TestSerial";
    private static final String SERIAL_PORT_PATH = "/dev/ttyS4";
    private static final int SERIAL_PORT_BAUDRATE = 9600;

    private static final String SEND_CMD="112233445566";


    Button mOpenBtn;
    Button mCloseBtn;
    Button mSendMsgBtn;
    TextView mInfoView;
    int fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_port_main);

        mOpenBtn = (Button)findViewById(R.id.open_serial_port);
        mCloseBtn = (Button)findViewById(R.id.close_serial_port);
        mSendMsgBtn = (Button)findViewById(R.id.send_msg);
        mInfoView = (TextView)findViewById(R.id.testinfo);

        mOpenBtn.setOnClickListener(ocl);
        mCloseBtn.setOnClickListener(ocl);
        mSendMsgBtn.setOnClickListener(ocl);
    }

    OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.open_serial_port:
                    mInfoView.setText("");
                    mInfoView.append("开始打开串口\n");
                    fd = SerialControl.serialOpen(SERIAL_PORT_PATH, SERIAL_PORT_BAUDRATE);
                    mInfoView.append(fd >= 0 ?"打开成功\n":"打开失败\n");
                    break;
                case R.id.send_msg:
                    mInfoView.append("\n发送CMD=112233445566\n");
                    if(fd >=0 )SerialControl.serialPuts(fd, SEND_CMD);
                    break;
                case R.id.close_serial_port:
                    if(fd >= 0)
                        SerialControl.serialClose(fd);
                    fd = -1;
                    break;
                default:
                    break;
            }

        }
    };
}

