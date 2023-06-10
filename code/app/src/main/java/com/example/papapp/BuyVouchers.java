package com.example.papapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BuyVouchers extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private TextView quantityTextView;
    private Button increaseButton;
    private Button decreaseButton;
    private Button calculateButton;
    private Button purchaseButton;
    private TextView pointsTextView;
    private int quantity;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Set the theme based on the user's preference
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        setContentView(R.layout.buy_vouchers);

        // Initialize UI elements
        quantityTextView = findViewById(R.id.quantityTextView);
        increaseButton = findViewById(R.id.increaseButton);
        decreaseButton = findViewById(R.id.decreaseButton);
        calculateButton = findViewById(R.id.calculateButton);
        purchaseButton = findViewById(R.id.purchaseButton);
        pointsTextView = findViewById(R.id.pointsTextView);

        // Set initial quantity and points
        quantity = 0;
        points = 20;
        updateQuantityTextView();
        updatePointsTextView();

        // Set click listeners for increase and decrease buttons
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity < 80) {
                    quantity++;
                    updateQuantityTextView();
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    quantity--;
                    updateQuantityTextView();
                }
            }
        });

        // Set click listener for calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCost();
            }
        });

        // Set click listener for purchase button
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your purchase logic here
            }
        });
    }

    // Update the quantity text view with the current quantity value
    private void updateQuantityTextView() {
        quantityTextView.setText(String.valueOf(quantity));
    }

    // Update the points text view with the current points value and discount
    private void updatePointsTextView() {
        String pointsText = "Points: " + points + " (€" + (points * 0.10) + " discount)";
        pointsTextView.setText(pointsText);
    }

    // Calculate the cost and display a toast message
    private void calculateCost() {
        double totalPrice = quantity * 1; // Calculate the total price
        double finalPrice = totalPrice - (points * 0.10); // Deduct the points value from the total price

        String message;
        if (finalPrice > 0) {
            message = "The final price is " + finalPrice + "€";
        } else {
            message = "You have enough points to cover the cost.";
        }

        Toast.makeText(BuyVouchers.this, message, Toast.LENGTH_SHORT).show();
    }
}
