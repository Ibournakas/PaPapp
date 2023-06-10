package com.example.papapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Maps extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.maps);

        ImageButton mapActivity = findViewById(R.id.mapActivityButton);
        mapActivity.setOnClickListener(view -> {

            Intent intent = new Intent( this, MapsActivity.class);

            startActivity(intent);
        });

        ImageButton bookClassroom = findViewById(R.id.bookClassroomButton);
        bookClassroom.setOnClickListener(view -> {

            Intent intent = new Intent( this, BookClassroom.class);

            startActivity(intent);
        });
        ImageButton searchMap = findViewById(R.id.searchOnMapButton);
        searchMap.setOnClickListener(view -> {

            Intent intent = new Intent( this, SearchOnMap.class);

            startActivity(intent);
        });
    }
}
