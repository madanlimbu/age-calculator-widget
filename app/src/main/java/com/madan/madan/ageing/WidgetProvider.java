package com.madan.madan.ageing;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;


public class WidgetProvider extends AppWidgetProvider {

    //WHEN WIDGET CREATED/OPENED
    @Override
    public void onEnabled(Context context){
        Log.d("WidgetProvider", "Enabled widget.");

        super.onEnabled(context);
    }

    /**
     * create a broadcast and if it is from out app then manually call onUpdate
     * used when new date of birth is set to update dates in widget
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent){
        super.onReceive(context, intent);

        if (intent.getAction().equals(MainActivity.AGE_REMINDER_UPDATE_WIDGET)) {
            // Update all widgets at 12 AM
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), WidgetProvider.class.getName());
            int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);
            onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        DateStore storedob = new DateStore(context);
        String age = "Use App To Set Correct Date Of Birth";
        if(!storedob.getData().equals("")) {
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

        final int count = appWidgetIds.length;

        // Update all the instance of widget.
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.age_in_widget, age );

            setWidgetTheme(remoteViews, property); //Set the color to each of the widgets

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
            } else {
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
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
