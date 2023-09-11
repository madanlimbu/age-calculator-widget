package com.madan.madan.ageing;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to save date of birth in shared preference using context package name as identifier
 *
 */
public class DateStore {

    private Context mContext;

    /**
     * Supply context of the activity.
     *
     * @param context
     */
    public DateStore(Context context) {
        mContext = context;
    }

    /**
     * Get sharedpreference using context.packagename.
     *
     * @return
     */
    public SharedPreferences getSharedPref(){
        return mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
    }

    // Store settings in  shared preference using apply(background/asynchrnous).
    public void storeData(String data) { getSharedPref().edit().putString("dob", data).apply(); }
    public void storeBackgroundColor(String color) { getSharedPref().edit().putString("background", color). apply(); }
    public void storeForegroundColor(String color) { getSharedPref().edit().putString("foreground", color).apply(); }


    //Use the context and packagename again to retrieve the data saved in shared preference.
    public String getData(){ return getSharedPref().getString("dob", ""); }
    public String getBackgroundColor() { return getSharedPref().getString("background", ""); }
    public String getForegroundColor() { return getSharedPref().getString("foreground", ""); }


    //public void storeSettings(String settings) { getSharedPref().edit().putString("settings", settings).apply(); }
    //public String getSettings() { return getSharedPref().getString("settings", ""); }
    //Todo:: Store settings in as a JSON and export it out as a setting object.
}
