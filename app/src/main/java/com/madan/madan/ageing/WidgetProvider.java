package com.madan.madan.ageing;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.widget.RemoteViews;
import java.util.Calendar;


public class WidgetProvider extends AppWidgetProvider {
    public static String ACTION_AUTO_UPDATE_WIDGET = "ACTION_AUTO_UPDATE_WIDGET_AT_ZERO_AM";
    public static String CHANNEL_ID = "AGEING_NOTIFICATION_CHANNEL_ID";


    //WHEN WIDGET CREATED/OPENED
    @Override
    public void onEnabled(Context context){
        super.onEnabled(context);
        createAlarmIntent(context);
    }

    //creates alarm intent to go off at 12am
    public void createAlarmIntent(Context context){

        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(WidgetProvider.ACTION_AUTO_UPDATE_WIDGET);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                context, 0, intent, 0
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                   AlarmManager.RTC_WAKEUP,
                   calendar.getTimeInMillis(),
                   AlarmManager.INTERVAL_DAY,
                   alarmIntent
           );
    }

    //Notification on update, with backward compatibility.
    public void createNotification(Context context, String age){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ageNotification";
            String description = "Notification about current Age";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID )
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Age")
                .setContentText(age)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationCompat.notify(0, mBuilder.build());
    }


    //create a broadcast and if it is from out app then manually call onUpdate
    //used when new date of birth is set to update dates in widget
    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        //when 12 am call update widget
        if(intent.getAction().equals(WidgetProvider.ACTION_AUTO_UPDATE_WIDGET)||intent.getAction().equals(context.getPackageName())){
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
            int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);
            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }

        //on android restart, all alram is reset so need to set it again on boot event
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            createAlarmIntent(context);
        }
    }

    //Update the person age on every widget when onupdate intent received.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        DateStore storedob = new DateStore(context);
        String age = "Use App To Set Correct Date Of Birth";
        if(storedob.getData()!="") {
            age = new AgeCalculator().calculateAge(storedob.getData());
        }

        //Set custom property for the widget before they are applied.
        WidgetTheme property = new WidgetTheme();
        if (!storedob.getBackgroundColor().isEmpty()) {
            property.setBackgroundColor(storedob.getBackgroundColor());
        }
        if (!storedob.getForegroundColor().isEmpty()) {
            property.setForegroundColor(storedob.getForegroundColor());
        }

        createNotification(context, age);
        final int count = appWidgetIds.length;

        // Update all the instance of widget.
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.age_in_widget, age );

            setWidgetTheme(remoteViews, property); //Set the color to each of the widgets

            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent,
                        PendingIntent.FLAG_MUTABLE);
            }else {
                pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent,
                        0);
            }
            remoteViews.setOnClickPendingIntent(R.id.age_in_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    /**
     * Function to update widget theme with custom settings.
     *
     * @param remoteViews
     * @param property
     */
    public void setWidgetTheme(RemoteViews remoteViews, Theme property){
        remoteViews.setInt(R.id.age_in_widget, "setBackgroundColor", Color.parseColor(property.getBackgroundColor()));
        remoteViews.setTextColor(R.id.age_in_widget, Color.parseColor(property.getForegroundColor()));
    }


}
