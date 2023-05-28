package com.example.papapp;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;


public class Profile extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.profile);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }


}
