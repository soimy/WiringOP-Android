package com.example.demo;
import android.app.Activity;
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

public class TestSerialPort extends Activity{
    private static final String TAG = "TestSerial";
    private static final String SERIAL_PORT_PATH = "/dev/ttyS0";
    private static final int SERIAL_PORT_BAUDRATE = 115200;

    Button mOpenBtn;
    Button mCloseBtn;
    Button mSendMsgBtn;
    TextView mInfoView, mInfoView1;
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
        mInfoView1 = (TextView)findViewById(R.id.testinfo1);
        et_serial_data = findViewById(R.id.et_serial_data);
        sn_uart_dev = findViewById(R.id.sn_uart_dev);
        mOpenBtn.setOnClickListener(ocl);
        mCloseBtn.setOnClickListener(ocl);
        mSendMsgBtn.setOnClickListener(ocl);

        uartString = RootCmd.execRootCmd("ls /dev/ttyS*");
//        uartString = uart.split("\\s+");
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
                    mInfoView1.setText("");
                    //mInfoView.append("open serial\n");
                    //fd = SerialControl.serialOpen(uart_dev, SERIAL_PORT_BAUDRATE);
                    fd = wpiControl.serialOpen(uart_dev, SERIAL_PORT_BAUDRATE);
                    if(fd > 0){
                        mCloseBtn.setEnabled(true);
                        mOpenBtn.setEnabled(false);
                        Toast toast=Toast.makeText(getApplicationContext(), "Open Success", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        Toast toast=Toast.makeText(getApplicationContext(), "Open Fail", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    //mInfoView.append(fd >= 0 ?"open success\n":"open fail\n");
                    //mInfoView1.append("aaaaa");
                    break;
                case R.id.send_msg:
                    String msg = et_serial_data.getText().toString();
                    mInfoView.append("send: " + msg + "\n");
                    if(fd >=0 ) {
                        wpiControl.serialPuts(fd, msg);
                        handler = new Handler();
                        String str;
                        mInfoView1.append("receive： ");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int avail = wpiControl.serialDataAvail(fd);
                                if(avail == -1)
                                    return ;
                                while(wpiControl.serialDataAvail(fd) > 0)
                                {
                                    int val = wpiControl.serialGetchar(fd);
                                    String str = new StringBuffer().append((char)val).toString();
                                    Log.i(TAG, "postDelayed... val = " + val + ", str = " + str);
                                    mInfoView1.append(str);
                                }
                                //handler.postDelayed(this,50);
                                mInfoView1.append("\n");
                            }
                        }, 200);
                    }
                    break;
                case R.id.close_serial_port:
                    if(fd >= 0) {
                        wpiControl.serialClose(fd);
                        mCloseBtn.setEnabled(false);
                        mOpenBtn.setEnabled(true);
                        mInfoView.setText("");
                        mInfoView1.setText("");
                    }
                    fd = -1;
                    break;
                default:
                    break;
            }

        }
    };
}

