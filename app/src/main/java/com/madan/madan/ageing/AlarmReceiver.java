package com.madan.madan.ageing;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

/**
 * Alarm Receiver (Broadcast Receiver) - Handles intent at 12 AM and Reboot.
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MainActivity.AGE_REMINDER_ZERO_AM)) {
            // At 12 AM - Create notification, Update Widgets and Create new Alarm Intent for new day.
            Notification n = new Notification(context);
            n.createNotification();
            this.updateWidgets(context);
            this.createAlarmIntent(context);
        }

        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            // On Android reboot - set the Alarm, old one will be replaced.
            this.createAlarmIntent(context);
        }
    }

    /**
     * Create Alarm Intent to go off at 12 AM.
     *
     * @param context
     */
    public void createAlarmIntent(Context context){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(MainActivity.AGE_REMINDER_ZERO_AM);
        PendingIntent alarmIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        } else {
            alarmIntent =  PendingIntent.getBroadcast(context, 0, intent, 0);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmIntent
            );
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            );
        }
    }

    /**
     * Broadcast intent for widget to update.
     *
     * @param context
     */
    public void updateWidgets(Context context) {
        Intent updateIntent = new Intent(context, WidgetProvider.class);
        updateIntent.setAction(MainActivity.AGE_REMINDER_UPDATE_WIDGET);
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(context, 0, updateIntent, PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(context, 0, updateIntent, 0);
        }
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }
}
