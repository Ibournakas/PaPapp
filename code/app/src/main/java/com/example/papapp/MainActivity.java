package com.example.papapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    private CheckBox mRememberMeCheckbox;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn",false)) {
            setTheme(R.style.Theme_Dark);
        }
        else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.activity_login);

        // Initialize views
        mEmailEditText = findViewById(R.id.editText1);
        mPasswordEditText = findViewById(R.id.editText2);
        mLoginButton = findViewById(R.id.button);
        mRememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
        // Create a shared preferences instance
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        int textColor = getResources().getColor(sharedPreferences.getBoolean("isDarkModeOn", false) ? R.color.white : R.color.black);
        mEmailEditText.setTextColor(textColor);
        mPasswordEditText.setTextColor(textColor);

        //if the user opens the app and he had ticked the remember me then auto login.
        if (sharedPreferences.getBoolean("rememberMe",true) && sharedPreferences.getBoolean("loggedIn", false)) {
            String email = sharedPreferences.getString("email", "");
            String password = sharedPreferences.getString("password", "");

            mEmailEditText.setText(email);
            mPasswordEditText.setText(password);

            // Login automatically
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mLoginButton.performClick();
                    return true;
                }
                return false;
            }
        });
        // Set click listener for login button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                // Get user input
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                // Validate user input
                if (TextUtils.isEmpty(email)) {
                    mEmailEditText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPasswordEditText.setError("Password is required");
                    return;
                }

                // Authenticate user
                if (email.equals("admin") && password.equals("admin")) {
                    // Login successful
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Check if remember me checkbox is checked
                    boolean rememberMe = mRememberMeCheckbox.isChecked();
                    boolean loggedIn =true;

                    SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.putBoolean("rememberMe", rememberMe);
                    editor.putBoolean("loggedIn", loggedIn);
                    editor.apply();

                    //Toast.makeText(MainActivity.this, sharedPreferences.getString("email", ""), Toast.LENGTH_SHORT).show();

                    // Start main activity
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Login failed
                    Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

