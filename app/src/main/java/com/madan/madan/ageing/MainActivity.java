package com.madan.madan.ageing;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
      private Button saveDate; //button that saves the date of birth to shared peference
      private TextView dateOfBirth; //input of date of birth
      private TextView result; //calculation result from date of birth to current date
      private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setFields();
        setSaveDateListener();
    }

    //set the view field to a variable for later use
    public void setFields(){
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "mono.ttf");


        this.saveDate = (Button) findViewById(R.id.saveDate);
        this.dateOfBirth = (TextView) findViewById(R.id.date);
        this.result = (TextView) findViewById(R.id.result);
        this.textView = (TextView) findViewById(R.id.textView);

        if(!getDateOfBirth().equals("")){
            this.dateOfBirth.setText(getDateOfBirth());
        }

        this.saveDate.setTypeface(font, Typeface.NORMAL);
        this.dateOfBirth.setTypeface(font, Typeface.NORMAL);
        this.result.setTypeface(font, Typeface.BOLD);
        this.textView.setTypeface(font, Typeface.NORMAL);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        this.dateOfBirth.setHint(dateFormat.format(date));

        DateStore storedob = new DateStore(getBaseContext());
    }

    //set listener to button, on click
    public void setSaveDateListener(){
        this.saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDateOfBirth();
            }
        });
    }

    //when save button clicked, save the date of birth in shared preference for later use in widget
    public void saveDateOfBirth(){
        String dob =  this.dateOfBirth.getText().toString();
        DateStore storedob = new DateStore(getBaseContext());
        storedob.storeData(dob);
        this.result.setText(new AgeCalculator().calculateAge(dob));
        updateExistingWidgetDates();
    }

    public String getDateOfBirth(){
        DateStore storedob = new DateStore(getBaseContext());
        return storedob.getData();
    }

    //updates age of all widget currently opened by boardcasting for widgetprovider
    public void updateExistingWidgetDates(){
        Intent updateIntent = new Intent(getBaseContext(), WidgetProvider.class);
        updateIntent.setAction(getBaseContext().getPackageName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, updateIntent,0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }



}
