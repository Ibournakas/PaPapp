package com.example.papapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class waitTimeDelay extends AppCompatActivity {

    private SharedPreferences sharedPreferences;


    private long lastDate;
    private float delay_bus;
    private  float delay_train;
    private TextView traindelay;
    private TextView busdelay;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.wait_time_delay);

        traindelay = findViewById(R.id.trainDelayValueTextView);
        busdelay = findViewById(R.id.busDelayValueTextView);




        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        // Check if an hour has passed since last delay generation
        long currentTime= currentDate.getTimeInMillis();
//        SharedPreferences.Editor editor1 = sharedPreferences.edit();
//        editor1.putLong("lastDate",currentTime);
//        editor1.apply();

        lastDate = sharedPreferences.getLong("lastDate", 0);
        delay_bus = sharedPreferences.getFloat("delay_bus", 0);

        delay_train = sharedPreferences.getFloat("delay_train", 0);

        if (currentTime - lastDate >= 60 * 60 * 1000) {

            long elapsedTimeMillis = currentTime - lastDate;
            float elapsedTimeHours = (float) Math.floor(elapsedTimeMillis / (1000f * 60 * 60));


            delay_bus += elapsedTimeHours * 0.5f;

            delay_train += elapsedTimeHours * 0.5f;

            // Update the last date in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("lastDate", currentTime);
            editor.putFloat("delay_train", delay_train);
            editor.putFloat("delay_bus", delay_bus);
            editor.apply();
        } else {
            // Retrieve the existing delay value
            delay_bus = sharedPreferences.getFloat("delay_bus", 0);

            delay_train = sharedPreferences.getFloat("delay_train", 0);
        }

        if (lastDate < currentTime) {
            // Generate a new delay value
            Random random = new Random();
            delay_bus = random.nextInt(10) + 1;
            delay_train = random.nextInt(10) + 1;

            // Update the last date in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("lastDate",currentTime);
            editor.putFloat("delay_train", delay_train);
            editor.putFloat("delay_bus", delay_bus);
            editor.apply();
        } else {
            // Retrieve the existing delay value
            delay_bus = sharedPreferences.getFloat("delay_bus", 0);

            delay_train = sharedPreferences.getFloat("delay_train", 0);
        }




        // Display the delay
        busdelay.setText(String.valueOf(delay_bus));
        traindelay.setText(String.valueOf(delay_train));

    }

}
