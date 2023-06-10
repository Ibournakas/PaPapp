package com.example.papapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PublicTransport extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.transport);
        ImageButton stops = findViewById(R.id.show_stops);
        stops.setOnClickListener(view -> {

            Intent intent = new Intent( this, Stops.class);

            startActivity(intent);
        });

        ImageButton waitTime = findViewById(R.id.waitTime);
        waitTime.setOnClickListener(view -> {

            Intent intent = new Intent( this, waitTimeDelay.class);

            startActivity(intent);
        });
        ImageButton trainsched = findViewById(R.id.train_sched);
        trainsched.setOnClickListener(view -> {

            Intent intent = new Intent( this, Train_sched.class);

            startActivity(intent);
        });

    }
}
