package com.example.papapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Points extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private TextView userPointsTextView;
    private EditText pointsToSendEditText;
    private Button sendPointsButton;

    private int userPoints ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.points);


        userPointsTextView = findViewById(R.id.text_user_points);
        pointsToSendEditText = findViewById(R.id.edit_points_to_send);
        sendPointsButton = findViewById(R.id.button_send_points);

        userPoints = sharedPreferences.getInt("points", 200);

        userPointsTextView.setText("Your Points: " + userPoints);

        sendPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pointsToSendStr = pointsToSendEditText.getText().toString().trim();
                if (!pointsToSendStr.isEmpty()) {
                    int pointsToSend = Integer.parseInt(pointsToSendStr);
                    if (pointsToSend > userPoints) {
                        Toast.makeText(Points.this, "You don't have enough points to send.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Proceed with sending the points
                        Toast.makeText(Points.this, "Points sent successfully!", Toast.LENGTH_SHORT).show();
                        // Subtract the sent points from userPoints
                        userPoints -= pointsToSend;
                        userPointsTextView.setText("Your Points: " + userPoints);
                        pointsToSendEditText.getText().clear();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("points", userPoints);
                        editor.apply();
                    }
                } else {
                    Toast.makeText(Points.this, "Please enter the points to send.", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }
}
