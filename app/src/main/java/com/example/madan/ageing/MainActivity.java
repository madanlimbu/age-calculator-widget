package com.example.madan.ageing;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
      private Button saveDate; //button that saves the date of birth to shared peference
      private TextView dateOfBirth; //input of date of birth
      private TextView result; //calculation result from date of birth to current date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFields();
        setSaveDateListener();
    }

    //set the view field to a variable for later use
    public void setFields(){
        this.saveDate = (Button) findViewById(R.id.saveDate);
        this.dateOfBirth = (TextView) findViewById(R.id.date);
        this.result = (TextView) findViewById(R.id.result);
        DateStore storedob = new DateStore(getBaseContext());
        this.saveDate.setText(storedob.getData());
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
