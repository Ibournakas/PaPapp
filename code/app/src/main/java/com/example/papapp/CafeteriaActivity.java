package com.example.papapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class CafeteriaActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn",false)) {
            setTheme(R.style.Theme_Dark);
        }
        else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.cafeteria);

        ImageButton cafeteria = findViewById(R.id.cafeMenu);
        cafeteria.setOnClickListener(view -> {

            Intent intent= new Intent(this, CafeMenu.class);
            startActivity(intent);
        });
        ImageButton buyCoupons = findViewById(R.id.coupon);
        buyCoupons.setOnClickListener(view -> {

            Intent intent= new Intent(this, BuyVouchers.class);
            startActivity(intent);
        });


    }
}
