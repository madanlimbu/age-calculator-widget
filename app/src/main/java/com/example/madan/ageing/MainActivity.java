package com.example.madan.ageing;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
     Button saveDate;
     TextView dateOfBirth;
     TextView result;
     SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveDate = (Button) findViewById(R.id.saveDate);
        dateOfBirth = (TextView) findViewById(R.id.date);
        result = (TextView) findViewById(R.id.result);
        saveDate.setText(getDate());
        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dob =  dateOfBirth.getText().toString();
                AgeCalculator calculator = new AgeCalculator();
                String age = calculator.calculateAge(dob);
                saveDate(dob);
                result.setText(age);
            }
        });

    }

    public void saveDate(String date){
        sharedPreferences = getSharedPreferences("StoreDob", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("dob", date);
        editor.commit();
    }

    public String getDate(){
        sharedPreferences = getSharedPreferences("StoreDob", Context.MODE_PRIVATE);
        String dob = (sharedPreferences.getString("dob", ""));
        return dob;
    }


}
