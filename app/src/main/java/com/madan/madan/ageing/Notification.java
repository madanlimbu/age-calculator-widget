package com.madan.madan.ageing;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification {
    public static String CHANNEL_ID = "AGEING_NOTIFICATION_CHANNEL_ID";

    private Context context;
    private String text;
    private String title;
    private DateStore store;

    public Notification(Context context) {
        this.context = context;
        this.store = new DateStore(context);
        this.title = "Age";
        this.text = "Use App To Set Correct Date Of Birth";
    }

    public void createNotification(){
        if(!this.store.getData().equals("")) {
            this.text = new AgeCalculator().calculateAge(this.store.getData());
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "ageNotification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification about current Age");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(this.context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.context, CHANNEL_ID )
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(this.title)
                .setContentText(this.text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        notificationCompat.notify(0, mBuilder.build());
    }
}
