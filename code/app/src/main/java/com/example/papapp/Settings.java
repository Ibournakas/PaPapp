package com.example.papapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class Settings extends AppCompatActivity {
    private String originalLanguage="English";
    private Boolean languageChanged = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Spinner spinnerLanguages = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);

        // Set the default selection to the original language
        String[] languages = getResources().getStringArray(R.array.languages);
        int originalLanguageIndex = Arrays.asList(languages).indexOf(originalLanguage);
        spinnerLanguages.setSelection(originalLanguageIndex);


        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String languageSelected = parent.getItemAtPosition(position).toString();
                if (!languageSelected.equals(originalLanguage) && languageChanged==false) {
                    languageChanged = true;
                    Toast.makeText(Settings.this, "Language changed to " + languageSelected, Toast.LENGTH_SHORT).show();
                }
                else if (languageChanged==true){
                    Toast.makeText(Settings.this, "Language changed to " + languageSelected, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button logout = findViewById(R.id.button2);
        logout.setOnClickListener(view -> {
                // Clear any user authentication data (e.g. session, token, etc.)

                // Navigate back to the login screen
                Intent intent = new Intent(Settings.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        });
    };
}
