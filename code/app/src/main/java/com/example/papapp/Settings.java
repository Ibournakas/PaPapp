package com.example.papapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;

public class Settings extends AppCompatActivity {
    private Boolean mode;
    private SharedPreferences sharedPreferences;
    private Boolean languageChanged = false;
    private SwitchCompat switchCompat;
    private String  languageSelected;

    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setLanguage() {
        String originalLanguage = sharedPreferences.getString("language", "English");
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = originalLanguage.equals("default") ? Locale.getDefault() : new Locale(originalLanguage);
        if (originalLanguage.equals("Greek") || originalLanguage.equals("Ελληνικά")) {

            configuration.setLocale(new Locale("el"));
        } else {
            configuration.setLocale(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        recreate();
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
        setLanguage();

        setContentView(R.layout.settings);

        Spinner spinnerLanguages = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);


        // Get the saved language selection from SharedPreferences

        String originalLanguage = sharedPreferences.getString("language", "English");
        // Set the default selection to the original language
        String[] languages = getResources().getStringArray(R.array.languages);
        int originalLanguageIndex = Arrays.asList(languages).indexOf(originalLanguage);
        spinnerLanguages.setSelection(originalLanguageIndex);
        //Toast.makeText(Settings.this, sharedPreferences.getString("language",""), Toast.LENGTH_SHORT).show();
        // Set the language based on the selected language in the spinner



//        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
        switchCompat = findViewById(R.id.switch2);
        switchCompat.setChecked(isDarkModeOn); // Set the switch state
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state of the switch to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isDarkModeOn", isChecked);
                mode=isChecked;
                editor.apply();
                // Restart the activity to apply the new theme
                recreate();
            }
        });



        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageSelected = parent.getItemAtPosition(position).toString();
                if (!languageSelected.equals(originalLanguage) && languageChanged == false) {
                    languageChanged = true;
                    Toast.makeText(Settings.this, "Language changed to " + languageSelected, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("language", languageSelected);
                    editor.apply();
                    // Set the language based on the selected language in the spinner
                    setLanguage();

                } else if (languageChanged == true) {
                    Toast.makeText(Settings.this, "Language changed to " + languageSelected, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("language", languageSelected);
                    editor.apply();
                    // Set the language based on the selected language in the spinner
                    setLanguage();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Button logout = findViewById(R.id.button2);
        logout.setOnClickListener(view -> {
                // Clear any user authentication data (e.g. session, token, etc.)
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("rememberMe");
                editor.remove("loggedIn");
                editor.putBoolean("isDarkModeOn", switchCompat.isChecked());
                editor.putString("language",languageSelected);
                editor.apply();

                // Navigate back to the login screen
                Intent intent = new Intent(Settings.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        });
    };
}
