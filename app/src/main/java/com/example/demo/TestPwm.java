package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;


public class TestPwm extends AppCompatActivity {
    private static final String TAG = "TestPwm";
    private EditText et_pwm_period;
    private SeekBar sb_pwm_duty;
    private CheckBox cb_pwm_en;
    private ToggleButton bt_pwm;
    private Spinner sp_pwm;
    private EditText pwm_controler;
    private TextView tv_duty1;
    String [] pwmString;
    String pwm_dev;
    private TextView tv_pwm_duty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwm_test);
        et_pwm_period = findViewById(R.id.et_pwm_period);
        sb_pwm_duty = findViewById(R.id.sb_pwm_duty);
        cb_pwm_en = findViewById(R.id.cb_pwm_en);
        bt_pwm = findViewById(R.id.btn_pwm);
        sp_pwm = findViewById(R.id.sp_pwm);
        pwm_controler =findViewById(R.id.pwm_controler);
        tv_duty1 = findViewById(R.id.tv_duty1);

        cb_pwm_en.setEnabled(false);
        sb_pwm_duty.setEnabled(false);
        pwmString = RootCmd.execRootCmd("ls /sys/class/pwm");
        //pwmString = pwm.split("\\s+");
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_dropdown,pwmString);
        sp_pwm.setAdapter(startAdapter);
        sp_pwm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pwm_dev = pwmString[i];
                String[] pwm_con = RootCmd.execRootCmd("ls -ld /sys/class/pwm/" + pwm_dev + "/device|awk '{print $NF}' |xargs basename");
                pwm_controler.setText(pwm_con[0]);
                tv_duty1.setText(pwm_dev + " Duty");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                pwm_dev = pwmString[0];
            }
        });
        bt_pwm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sb_pwm_duty.setEnabled(false);
                    cb_pwm_en.setEnabled(false);
                    RootCmd.execRootCmdSilent("echo 0 >  /sys/class/pwm/"+ pwm_dev + "/unexport");
                } else {
                    sb_pwm_duty.setEnabled(true);
                    cb_pwm_en.setEnabled(true);
                    sb_pwm_duty.setMax(Integer.parseInt(et_pwm_period.getText().toString()));
                    RootCmd.execRootCmdSilent("echo 0 >  /sys/class/pwm/"+ pwm_dev + "/export");
                }
            }
        });
        cb_pwm_en.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    RootCmd.execRootCmdSilent("echo 1 >  /sys/class/pwm/" + pwm_dev + "/pwm0/enable");
                    String[] str = RootCmd.execRootCmd("cat /sys/class/pwm/" + pwm_dev + "/pwm0/enable");
                    if(str != null) {
                        Log.i(TAG, "str = " + str[0]);
                        if (!"1".equals(str[0])) {
                            Log.i(TAG, "straaa = " + str[0]);
                            cb_pwm_en.setChecked(false);
                        }
                    }
                }
                else {
                    RootCmd.execRootCmdSilent("echo 0 >  /sys/class/pwm/" + pwm_dev + "/pwm0/enable");
                }
            }
        });

        sb_pwm_duty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int duty;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                System.out.println("onProgressChanged");
                duty = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("onStartTrackingTouch");
                String cmd = "echo " + seekBar.getMax() + " > /sys/class/pwm/" + pwm_dev + "/pwm0/period";
                RootCmd.execRootCmdSilent(cmd);
                System.out.println("onStartTrackingTouch end");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("onStopTrackingTouch");
                String cmd = "echo " + duty + " > /sys/class/pwm/" + pwm_dev + "/pwm0/duty_cycle";
                RootCmd.execRootCmdSilent(cmd);
            }
        });
    }
}

