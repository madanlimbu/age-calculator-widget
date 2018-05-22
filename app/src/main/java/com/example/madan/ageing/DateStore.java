package com.example.madan.ageing;

import android.content.Context;
import android.content.SharedPreferences;

//Class to save date of birht in shared peference using context package name as identifier
public class DateStore {

    private Context mContext;
    //supply context of the activity
    public DateStore(Context context) {
        mContext = context;
    }

    //get sharedpreference using context.packagename
    public SharedPreferences getSharedPref(){
        return mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
    }

    //store dob in  shared peference using apply(background/asynchrnous)
    public void storeData(String data) {
        getSharedPref().edit().putString("dob", data).apply();
    }

    //use the context and packagename again to retrive the data saved in shared preference
    public String getData(){
        return getSharedPref().getString("dob", "");
    }

}
