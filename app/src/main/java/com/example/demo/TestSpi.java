package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wiringop.wpiControl;

import java.io.UnsupportedEncodingException;


public class TestSpi extends AppCompatActivity {
    private static final String TAG = "TestSpi";
    EditText spi_port_text;
    EditText spi_channel_text;
    EditText spi_speed_text;

    Button setup_spi_btn;
    EditText spi_data0_text;
    EditText spi_data1_text;
    EditText spi_data2_text;
    EditText spi_data3_text;

    Button start_spi_btn;
    TextView testinfo;
    Spinner spidev;
    String spi_device;
    String [] spString;
    int channel, port, speed;

    int fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spi_main);

        spi_channel_text = (EditText)findViewById(R.id.spi_channel_text);
        spi_speed_text = (EditText)findViewById(R.id.spi_speed_text);
        spi_port_text = (EditText)findViewById(R.id.spi_port_text);
        setup_spi_btn = (Button)findViewById(R.id.setup_spi_btn);
        spidev = (Spinner)findViewById(R.id.spidev);

        spi_data0_text = (EditText)findViewById(R.id.spi_data0_text);
        spi_data1_text = (EditText)findViewById(R.id.spi_data1_text);
        spi_data2_text = (EditText)findViewById(R.id.spi_data2_text);
        spi_data3_text = (EditText)findViewById(R.id.spi_data3_text);

        start_spi_btn = (Button)findViewById(R.id.start_spi_btn);
        testinfo = (TextView) findViewById(R.id.testinfo);

        setup_spi_btn.setOnClickListener(ocl);
        start_spi_btn.setOnClickListener(ocl);
        spString = RootCmd.execRootCmd("ls /dev/spidev*");
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_dropdown,spString);
        spidev.setAdapter(startAdapter);
        spidev.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spi_device = spString[i];
                speed = Integer.parseInt(spi_speed_text.getText().toString());
                channel = Integer.valueOf(spi_device.substring(spi_device.length()-3, spi_device.length() - 2));
                port = Integer.valueOf(spi_device.substring(spi_device.length()-1));
                spi_port_text.setText(String.valueOf(port));
                spi_channel_text.setText(String.valueOf(channel));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spi_device = spString[0];
                speed = Integer.parseInt(spi_speed_text.getText().toString());
                channel = Integer.valueOf(spi_device.substring(spi_device.length()-3, spi_device.length() - 2));
                port = Integer.valueOf(spi_device.substring(spi_device.length()-1));
                spi_port_text.setText(String.valueOf(port));
                spi_channel_text.setText(String.valueOf(channel));
            }
        });
    }

    View.OnClickListener ocl = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.setup_spi_btn:
                    String cmd = "chmod 666 " +  spi_device;
                    RootCmd.execRootCmdSilent(cmd);
                    Log.i(TAG, "channel = " + channel + ", port = " + port);
                    fd = wpiControl.wiringPiSPISetupMode(channel, port, speed, 0);
                    if(fd != -1)
                        testinfo.setText("SPI Open Success, channel: " + channel+ ", port: " + port + ", speed:" + speed);
                    else
                        testinfo.setText("SPI Open Success, channel: " + channel+ ", port: " + port + ", speed:" + speed);

                    break;
                case R.id.start_spi_btn:
                    if(fd != -1){
                        byte[] data = {9, 9, 9, 9};
                        data[0] = (byte)(Integer.valueOf(spi_data0_text.getText().toString().substring(2),16) & 0xff);
                        data[1] = (byte)(Integer.valueOf(spi_data1_text.getText().toString().substring(2),16) & 0xff);
                        data[2] = (byte)(Integer.valueOf(spi_data2_text.getText().toString().substring(2),16) & 0xff);
                        data[3] = (byte)(Integer.valueOf(spi_data3_text.getText().toString().substring(2),16) & 0xff);
                        int ret = wpiControl.wiringPiSPIDataRW(channel, data, 4);
                        if(ret == -1)
                            testinfo.append("SPI Transfer fail\n");
                        else{
                            String str = null;
                            try {
                                str = new String(data, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            testinfo.setText("SPI Transfer success\n"+"ret:"+ ret+"\ndata[0]:"+ Integer.toHexString(data[0] & 0xff) +"\ndata[1]:"+Integer.toHexString(data[1] & 0xff)+"\ndata[2]:"+Integer.toHexString(data[2] & 0xff)+"\ndata[3]:"+Integer.toHexString(data[3] & 0xff));

                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };
}

