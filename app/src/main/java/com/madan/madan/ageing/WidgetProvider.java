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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Random;

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

        Intent intent = new Intent(WidgetProvider.ACTION_AUTO_UPDATE_WIDGET);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(
                context, 0, intent, 0
        );

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                alarmIntent
        );
    }

    //Notification on update, with backward compability
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

    //update the person age on every widget when onupdate intent recived
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        DateStore storedob = new DateStore(context);
        String age = "Use App To Set Correct Date Of Birth";
        if(storedob.getData()!="") {
            age = new AgeCalculator().calculateAge(storedob.getData());
        }

        createNotification(context, age);
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.age_in_widget, age + Calendar.getInstance().getTimeInMillis());

          //  Intent intent = new Intent(context, WidgetProvider.class);
           // intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
             //       0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.age_in_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }


}
