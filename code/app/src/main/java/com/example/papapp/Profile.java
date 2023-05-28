package com.example.papapp;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Profile extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

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
        // Retrieve the values from SharedPreferences
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");

// Set the values in the EditText fields
        EditText usernameEditText = findViewById(R.id.usernameText);
        EditText emailEditText = findViewById(R.id.emailText);
        EditText passwordEditText = findViewById(R.id.passwordText);

        usernameEditText.setText(username);
        emailEditText.setText(email);
        passwordEditText.setText(password);

        Button changepic = findViewById(R.id.changePictureButton);
        changepic.setOnClickListener(view -> {


        });

        Button changeuser = findViewById(R.id.changeUsernameButton);
        changeuser.setOnClickListener(view -> {
            String newUsername = usernameEditText.getText().toString().trim();

            if (!newUsername.isEmpty()) {
                // Save the updated username in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", newUsername);
                editor.apply();
                Toast.makeText(this, "Username updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
            }
        });

        Button changeemail = findViewById(R.id.changeEmailButton);
        changeemail.setOnClickListener(view -> {
            String newEmail = emailEditText.getText().toString().trim();

            if (!newEmail.isEmpty()) {
                // Save the updated email in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", newEmail);
                editor.apply();
                Toast.makeText(this, sharedPreferences.getString("email", ""), Toast.LENGTH_SHORT).show();

                Toast.makeText(this, "Email updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });

        Button changepass = findViewById(R.id.changePasswordButton);
        changepass.setOnClickListener(view -> {
            String newPassword = passwordEditText.getText().toString().trim();

            if (!newPassword.isEmpty()) {
                // Save the updated password in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", newPassword);
                editor.apply();
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a valid password", Toast.LENGTH_SHORT).show();
            }
        });







    }


}
