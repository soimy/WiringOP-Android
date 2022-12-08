package com.example.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wiringop.SpiControl;

import java.io.UnsupportedEncodingException;


public class TestSpi extends Activity {
    private static final String TAG = "TestSpi";
    EditText spi_port_text;
    EditText spi_speed_text;

    Button setup_spi_btn;
    EditText spi_data0_text;
    EditText spi_data1_text;
    EditText spi_data2_text;
    EditText spi_data3_text;

    Button start_spi_btn;
    TextView testinfo;



    int fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spi_main);

        spi_port_text = (EditText)findViewById(R.id.spi_port_text);
        spi_speed_text = (EditText)findViewById(R.id.spi_speed_text);

        setup_spi_btn = (Button)findViewById(R.id.setup_spi_btn);

        spi_data0_text = (EditText)findViewById(R.id.spi_data0_text);
        spi_data1_text = (EditText)findViewById(R.id.spi_data1_text);
        spi_data2_text = (EditText)findViewById(R.id.spi_data2_text);
        spi_data3_text = (EditText)findViewById(R.id.spi_data3_text);

        start_spi_btn = (Button)findViewById(R.id.start_spi_btn);
        testinfo = (TextView) findViewById(R.id.testinfo);

        setup_spi_btn.setOnClickListener(ocl);
        start_spi_btn.setOnClickListener(ocl);

    }

    View.OnClickListener ocl = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.setup_spi_btn:
                    int channel = Integer.parseInt(spi_port_text.getText().toString());
                    int speed = Integer.parseInt(spi_speed_text.getText().toString());
                    fd = SpiControl.wiringPiSPISetup(channel, speed);
                    if(fd != -1)
                        testinfo.setText("SPI初始化成功, channel:" + channel+ ", speed:" + speed);
                    else
                        testinfo.setText("SPI初始化失败, channel:" + channel+ ", speed:" + speed);

                    break;
                case R.id.start_spi_btn:
                    if(fd != -1){
                        byte[] data = {9, 9, 9, 9};
                      //  data[0] = spi_data0_text.getText().toString().getBytes()[0];
                        //data[1] = spi_data1_text.getText().toString().getBytes()[0];
                        //data[2] = spi_data2_text.getText().toString().getBytes()[0];
                        //data[3] = spi_data3_text.getText().toString().getBytes()[0];
                        data[0] = (byte)(Integer.valueOf(spi_data0_text.getText().toString().substring(2),16) & 0xff);
                        data[1] = (byte)(Integer.valueOf(spi_data1_text.getText().toString().substring(2),16) & 0xff);
                        data[2] = (byte)(Integer.valueOf(spi_data2_text.getText().toString().substring(2),16) & 0xff);
                        data[3] = (byte)(Integer.valueOf(spi_data3_text.getText().toString().substring(2),16) & 0xff);
                        int ret = SpiControl.wiringPiSPIDataRW(Integer.parseInt(spi_port_text.getText().toString()), data, 4);
                        if(ret == -1)
                            testinfo.append("SPI传输失败");
                        else{
                            String str = null;
                            try {
                                str = new String(data, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            testinfo.setText("SPI传输成功"+"ret:"+ret+", data[0]:"+ Integer.toHexString(data[0] & 0xff) +", data[1]:"+Integer.toHexString(data[1] & 0xff)+", data[2]:"+Integer.toHexString(data[2] & 0xff)+", data[3]:"+Integer.toHexString(data[3] & 0xff));

                        }
                    }


                 //   testinfo.append(fd >= 0 ?"打开成功\n":"打开失败\n");
                    break;

                default:
                    break;
            }

        }
    };
}

