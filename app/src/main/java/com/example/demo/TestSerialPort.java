package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wiringop.wpiControl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TestSerialPort extends AppCompatActivity{
    private static final String TAG = "TestSerial";
    private static final String SERIAL_PORT_PATH = "/dev/ttyS0";
    private static final int SERIAL_PORT_BAUDRATE = 115200;

    Button mOpenBtn;
    Button mCloseBtn;
    Button mSendMsgBtn;
    TextView mInfoView;
    EditText et_baud;
    EditText et_serial_data;
    Handler handler;
    Spinner sn_uart_dev;
    String uart_dev;
    String [] uartString;
    int Cnt = 0;
    int fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_port_main);

        mOpenBtn = (Button)findViewById(R.id.open_serial_port);
        mCloseBtn = (Button)findViewById(R.id.close_serial_port);
        mSendMsgBtn = (Button)findViewById(R.id.send_msg);
        mInfoView = (TextView)findViewById(R.id.testinfo);
        et_serial_data = findViewById(R.id.et_serial_data);
        sn_uart_dev = findViewById(R.id.sn_uart_dev);
        et_baud = findViewById(R.id.serial_baud);
        mOpenBtn.setOnClickListener(ocl);
        mCloseBtn.setOnClickListener(ocl);
        mSendMsgBtn.setOnClickListener(ocl);

        uartString = RootCmd.execRootCmd("ls /dev/ttyS*");
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_dropdown,uartString);
        sn_uart_dev.setAdapter(startAdapter);
        sn_uart_dev.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                uart_dev = uartString[i];
                RootCmd.execRootCmdSilent("chmod 666 " + uart_dev);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    OnClickListener ocl = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.open_serial_port:
                    mInfoView.setText("");
                    int baud = Integer.valueOf(et_baud.getText().toString());
                    fd = wpiControl.serialOpen(uart_dev, baud);
                    if(fd > 0){
                        mCloseBtn.setEnabled(true);
                        mOpenBtn.setEnabled(false);
                        mSendMsgBtn.setEnabled(true);
                        Toast toast=Toast.makeText(getApplicationContext(), "Open Success", Toast.LENGTH_SHORT);
                        toast.show();
                        handler = new Handler();
                        String str;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int size = wpiControl.serialDataAvail(fd);
                                if( size > 0)
                                {
                                    byte [] b = new byte[size];
                                    for(int i=0; i<size; i++)
                                    {
                                        int val = wpiControl.serialGetchar(fd);
                                        b[i] = (byte)val;
                                        if(val == '\0')
                                            break;
                                    }
                                    String str = new String(b, StandardCharsets.UTF_8);
                                    //String str = new StringBuffer().append((char)val).toString();
                                    mInfoView.append(str);
                                }
                                handler.postDelayed(this,10);
                            }
                        }, 10);
                    }
                    else
                    {
                        Toast toast=Toast.makeText(getApplicationContext(), "Open Fail", Toast.LENGTH_SHORT);
                        mSendMsgBtn.setEnabled(false);
                        toast.show();
                    }
                    break;
                case R.id.send_msg:
                    String msg = et_serial_data.getText().toString();
                    //mInfoView.append("send: " + msg + "\n");
                    if(fd >=0 ) {
                        wpiControl.serialPuts(fd, msg);

                    }
                    break;
                case R.id.close_serial_port:
                    if(fd >= 0) {
                        wpiControl.serialClose(fd);
                        mCloseBtn.setEnabled(false);
                        mOpenBtn.setEnabled(true);
                        mSendMsgBtn.setEnabled(false);
                        mInfoView.setText("");
                    }
                    fd = -1;
                    break;
                default:
                    break;
            }

        }
    };
}

