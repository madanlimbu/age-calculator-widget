package com.example.madan.ageing;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

public class WidgetProvider extends AppWidgetProvider {

    //create a broadcast and if it is from out app then manually call onUpdate
    //used when new date of birth is set to update dates in widget
    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);
        if(intent.getAction().equals(context.getPackageName())){

            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
            int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);

            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);

        }
    }

    //update the person age on every update call
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        DateStore storedob = new DateStore(context);
        String age = "No Correct Date Of Birth Set Yet";
        if(storedob.getData()!="") {
            age = new AgeCalculator().calculateAge(storedob.getData());
        }
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.age_in_widget, age + " Test auto update : "+ number);

            Intent intent = new Intent(context, WidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.age_in_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }
}
