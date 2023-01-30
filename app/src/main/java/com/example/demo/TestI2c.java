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

public class TestI2c extends AppCompatActivity {
    private static final String TAG = "TestI2c";
    EditText i2c_addr_text;
    EditText i2c_reg_text;
    EditText i2c_value_text;

    Button setup_i2c_btn;
    Button read_i2c_btn;
    TextView read_i2c_tv;

    Button write_i2c_btn;
    TextView write_i2c_tv;
    TextView mtestinfo;
    Spinner sn_i2c;
    String [] i2cString;
    String i2c_dev;

    int fd, i2c_addr, reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i2c_main);

        i2c_addr_text = (EditText)findViewById(R.id.i2c_addr_text);

        setup_i2c_btn = (Button)findViewById(R.id.setup_i2c_btn);
        read_i2c_btn = (Button)findViewById(R.id.read_i2c_btn);
        read_i2c_tv = (TextView)findViewById(R.id.read_i2c_tv);

        i2c_reg_text = (EditText) findViewById(R.id.i2c_reg_text);
        write_i2c_btn = (Button)findViewById(R.id.write_i2c_btn);
        i2c_value_text = (EditText)findViewById(R.id.i2c_value_text);
        write_i2c_tv = (TextView)findViewById(R.id.write_i2c_tv);
        mtestinfo = (TextView)findViewById(R.id.testinfo);
        sn_i2c = findViewById(R.id.sn_i2c);

        read_i2c_btn.setOnClickListener(ocl);
        write_i2c_btn.setOnClickListener(ocl);
        setup_i2c_btn.setOnClickListener(ocl);

        i2cString = RootCmd.execRootCmd("ls /dev/i2c-*");
        //i2cString = i2c.split("\\s+");
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_dropdown,i2cString);
        sn_i2c.setAdapter(startAdapter);
        sn_i2c.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                i2c_dev = i2cString[i];
                RootCmd.execRootCmdSilent("chmod 666 " + i2c_dev);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    View.OnClickListener ocl = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            switch (arg0.getId()) {
                case R.id.setup_i2c_btn:

                    i2c_addr = Integer.valueOf(i2c_addr_text.getText().toString().substring(2),16);
                    Log.i(TAG,"---------addr:" + i2c_addr);


                    fd = wpiControl.wiringPiI2CSetupInterface(i2c_dev, i2c_addr);
                    if(fd >= 0)
                        mtestinfo.setText("open success; i2c channel：" + i2c_dev + ", addr: 0x" + Integer.toHexString(i2c_addr));

                    break;
                case R.id.read_i2c_btn:
                    if(fd >= 0) {
                        reg = Integer.valueOf(i2c_reg_text.getText().toString().substring(2),16);
                        int value = wpiControl.wiringPiI2CReadReg8(fd, reg);
                        if(value == -1)
                            read_i2c_tv.setText("read fail");
                        else
                            read_i2c_tv.setText("0x" + Integer.toHexString(value));

                    }

                 //   testinfo.append(fd >= 0 ?"打开成功\n":"打开失败\n");
                    break;
                case R.id.write_i2c_btn:
                    if(fd >= 0) {

                        reg = Integer.valueOf(i2c_reg_text.getText().toString().substring(2),16);
                        int value = Integer.valueOf(i2c_value_text.getText().toString().substring(2),16);
                        wpiControl.wiringPiI2CWriteReg8(fd, reg, value);
                        int ret = wpiControl.wiringPiI2CReadReg8(fd, reg);
                        if(ret == value)
                            write_i2c_tv.setText("write success");
                        else
                            write_i2c_tv.setText("write fail");
                    }
                    break;
                default:
                    break;
            }

        }
    };

}



