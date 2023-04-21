package com.example.papapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String login_name = sharedPreferences.getString("email", "");
        String username = String.format(getString(R.string.username1), login_name);
        TextView myTextView = findViewById(R.id.textView);
        myTextView.setText(username);


        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {

            Intent intent = new Intent( this, Settings.class);

            startActivity(intent);
        });


        



}}
