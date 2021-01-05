package com.example.project2.background;

        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;

        import java.util.Calendar;

        import static android.content.Context.ALARM_SERVICE;
        import static android.content.Context.MODE_PRIVATE;
        import static com.example.project2.Activities.SettingActivity.TIME_REMINDER_KEY;

public class ResetAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences =context.getSharedPreferences("settings", MODE_PRIVATE);

        String time = sharedPreferences.getString( TIME_REMINDER_KEY, null);
        if(time==null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
            calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(3, 5)));
            calendar.set(Calendar.SECOND, 0);

            Intent intentToOpenReminderReciver = new Intent(context, ReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }
}
