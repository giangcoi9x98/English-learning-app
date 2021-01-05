package com.example.project2.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project2.R;
import com.example.project2.background.ReminderReceiver;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.id_tiltle)
    TextView idTiltle;
    @BindView(R.id.iv_done)
    ImageView ivDone;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.tv_daily_remider_tiltle)
    TextView tvDailyRemiderTiltle;
    @BindView(R.id.sw_reminder)
    Switch swReminder;
    @BindView(R.id.tv_time_reminder)
    TextView tvTimeReninder;
    String time;
    public static final String TIME_REMINDER_KEY = "time reminder key";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String time = sharedPreferences.getString(TIME_REMINDER_KEY, null);
        if (time != null) {
            tvTimeReninder.setText(time);
            swReminder.setChecked(true);
        } else {
            tvTimeReninder.setVisibility(View.GONE);

        }

        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvTimeReninder.setVisibility(View.VISIBLE);
                } else {
                    tvTimeReninder.setVisibility(View.GONE);
                }

            }
        });

    }

    @OnClick({R.id.img_back, R.id.iv_done, R.id.sw_reminder, R.id.tv_time_reminder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.iv_done:
                saveSetting();
                break;
//            case R.id.sw_reminder:
//                setTimeReminder();
//                break;
            case R.id.tv_time_reminder:
                setTimeReminder();
                break;
        }
    }

    private void saveSetting() {
        if (time == null) {
            time = "00:00";
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3, 5)));
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (swReminder.isChecked()) {
            editor.putString(TIME_REMINDER_KEY, time);
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        } else {
            editor.putString(TIME_REMINDER_KEY, null);
            alarmManager.cancel(pendingIntent);
        }
        editor.commit();
        Toast.makeText(this, "Saving", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setTimeReminder() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = String.format("%02d:%02d", hourOfDay, minute);
                tvTimeReninder.setText(time);

            }
        }, currentHour, currentMin, true);
        timePickerDialog.show();
    }
}
