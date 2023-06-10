package com.example.papapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce = false;
    private Button manageRateButton;
    private SharedPreferences sharedPreferences;
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
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn",false)) {
            setTheme(R.style.Theme_Dark);
        }
        else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.home);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String login_name = sharedPreferences.getString("email", "");
        String savedUsername = sharedPreferences.getString("username", "");

        String username;
        if (savedUsername != null && !savedUsername.isEmpty()) {
            // Use the existing saved username
            username = savedUsername;
        } else {
            // Extract the username from the email
            if (login_name.contains("@")) {
                username = login_name.substring(0, login_name.indexOf("@"));
            } else {
                username = login_name;
            }
            // Save the username in SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();
        }


        String formattedUsername = String.format(getString(R.string.username1), username);
        TextView myTextView = findViewById(R.id.textView);
        myTextView.setText(formattedUsername);

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {

            Intent intent = new Intent( this, Settings.class);

            startActivity(intent);
        });

        Button  profile = findViewById(R.id.userButton);
        profile.setOnClickListener(view -> {

            Intent intent = new Intent( this, Profile.class);

            startActivity(intent);
        });


        ImageButton announcements = findViewById(R.id.imageButton5);
        announcements.setOnClickListener(view -> {

            Intent intent = new Intent( this, AnnouncementActivity.class);

            startActivity(intent);
        });

        ImageButton cafeteria = findViewById(R.id.imageButton7);
        cafeteria.setOnClickListener(view -> {

            Intent intent= new Intent(this, CafeteriaActivity.class);
            startActivity(intent);
        });

        ImageButton transport = findViewById(R.id.imageButton3);
        transport.setOnClickListener(view -> {

            Intent intent= new Intent(this, PublicTransport.class);
            startActivity(intent);
        });

        ImageButton library = findViewById(R.id.libraryButton);
        library.setOnClickListener(view -> {

            Intent intent= new Intent(this, Library.class);
            startActivity(intent);
        });

        ImageButton mapsButton = findViewById(R.id.mapButton);
        mapsButton.setOnClickListener(view -> {

            Intent intent= new Intent(this, Maps.class);
            startActivity(intent);
        });

    }
}
