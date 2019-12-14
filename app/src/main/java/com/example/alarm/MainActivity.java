package com.example.alarm;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {
   private static final int ALARM_REQUEST_CODE = 0;
   private TimePicker tpTime;
   private Button btnSetAlarm, btncancelAlarm;
   private TextView tvAlarmInfo;
   private PendingIntent curAlarmIntent;
   private AlarmManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tpTime = findViewById(R.id.tptime);
        btnSetAlarm =findViewById(R.id.button);
        btncancelAlarm= findViewById(R.id.button2);
        tvAlarmInfo = findViewById(R.id.textView);

        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        tpTime.setIs24HourView(true);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                curAlarmIntent = setAlarm(tpTime.getHour(),tpTime.getMinute());
                btncancelAlarm.setVisibility(View.VISIBLE);
            }
        });
        btncancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curAlarmIntent != null){
                    manager.cancel(curAlarmIntent);
                    Toast.makeText(MainActivity.this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
                    btncancelAlarm.setVisibility(View.GONE);
                    tvAlarmInfo.setText("Currently No Alarm is Set");
                }
            }
        });
    }

    private PendingIntent setAlarm(int hour, int minute){
        Intent intent = new Intent(this, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,ALARM_REQUEST_CODE,intent,PendingIntent.FLAG_CANCEL_CURRENT| PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);

        if(SDK_INT < Build.VERSION_CODES.KITKAT){
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
        }
        else if(Build.VERSION_CODES.KITKAT <= SDK_INT && SDK_INT< Build.VERSION_CODES.M){
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
        }
        else if(SDK_INT >= Build.VERSION_CODES.M){
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
        }
        tvAlarmInfo.setText("Alarm is Set at" +hour + ":" +minute + ":00");
        return pendingIntent;
    }
}
