package com.madan.madan.ageing;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
      public static String AGE_REMINDER_UPDATE_WIDGET = "com.madan.madan.ageing.AGE_REMINDER_UPDATE_WIDGET";
      public static String AGE_REMINDER_ZERO_AM = "com.madan.madan.ageing.AGE_REMINDER_ZERO_AM";

      private Button saveDate; //button that saves the date of birth to shared peference
      private TextView dateOfBirth; //input of date of birth
      private TextView result; //calculation result from date of birth to current date
      private TextView textView;
      private TextView backgroundColor;
      private DateStore db;
      private TextView foregroundColor;
      private TextView helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setDb();
        setFields();
        setSaveDateListener();
    }

    /**
     * Set the view field to a variable for later use.
     *
     */
    public void setFields(){
        this.saveDate = (Button) findViewById(R.id.saveDate);
        this.dateOfBirth = (TextView) findViewById(R.id.date);
        this.result = (TextView) findViewById(R.id.result);
        this.textView = (TextView) findViewById(R.id.textView);
        this.backgroundColor = (TextView) findViewById(R.id.backgroundColor);
        this.foregroundColor = (TextView) findViewById(R.id.foregroundColor);
        this.helper = (TextView) findViewById(R.id.helper);

        setSettingsFromDB();
        setFontStyle();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        this.dateOfBirth.setHint(dateFormat.format(date));
    }

    /**
     * Database connector initialise.
     *
     */
    public void setDb() {
        this.db = new DateStore(getBaseContext());
    }

    /**
     * Set Setting from the database.
     *
     */
    public void setSettingsFromDB() {
        if(!db.getData().equals("")){
            this.dateOfBirth.setText(db.getData());
        }
        if (!(db.getBackgroundColor() == "")) {
            this.backgroundColor.setText(db.getBackgroundColor());
        }
        if (!(db.getForegroundColor() == "")) {
            this.foregroundColor.setText(db.getForegroundColor());
        }
    }

    /**
     * Set listener to button, on click.
     *
     */
    public void setSaveDateListener(){
        this.saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDateOfBirth();
            }
        });
    }

    /**
     *  When save button clicked, save the dob and settings in shared preference for later use in widget.
     *
     */
    public void saveDateOfBirth(){
        String dob =  this.dateOfBirth.getText().toString();
        String bg = this.backgroundColor.getText().toString();
        String fg = this.foregroundColor.getText().toString();

        DateStore storedob = new DateStore(getBaseContext());
        storedob.storeData(dob);
        storedob.storeBackgroundColor(bg);
        storedob.storeForegroundColor(fg);

        this.result.setText(new AgeCalculator().calculateAge(dob));

        Notification n = new Notification(getBaseContext());
        n.createNotification();

        AlarmReceiver ar = new AlarmReceiver();
        ar.updateWidgets(getBaseContext());
        ar.createAlarmIntent(getBaseContext());
    }

    /**
     * Style the font.
     *
     */
    public void setFontStyle() {
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "mono.ttf");
        this.saveDate.setTypeface(font, Typeface.NORMAL);
        this.dateOfBirth.setTypeface(font, Typeface.NORMAL);
        this.result.setTypeface(font, Typeface.BOLD);
        this.textView.setTypeface(font, Typeface.NORMAL);
        this.backgroundColor.setTypeface(font, Typeface.NORMAL);
        this.foregroundColor.setTypeface(font, Typeface.NORMAL);
        this.helper.setTypeface(font, Typeface.NORMAL);
    }
}
