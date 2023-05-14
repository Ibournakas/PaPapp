package com.example.papapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.papapp.Announcement_Services.Announcements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CafeMenu  extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    final Calendar myCalendar= Calendar.getInstance();
    private EditText dateText;
    private String monthName;
    private String dayName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.cafemenu);

        DatePickerDialog.OnDateSetListener datePickerListener = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            try {
                updateDateLabel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            Log.d("CafeMenu", "Selected day name: " + dayName);
//
//            // use dayStr and monthStr variables as required
//            Log.d("CafeMenu", "Selected month: " + monthName);
        };


        dateText = findViewById(R.id.date_picker);
        dateText.setOnClickListener(v -> new DatePickerDialog(CafeMenu.this,datePickerListener,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        Spinner spinner = findViewById(R.id.menu_spinner);
        String[] options = {"Day", "Week"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spinner.setAdapter(adapter);
        spinner.setSelection(0); // Set "Day" as the default selection





    }
    private void updateDateLabel() throws IOException {
        if(dateText.getError() != null){
            dateText.setError(null);
        }
        String myFormat="dd-MM-yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.getDefault());
        dateText.setText(dateFormat.format(myCalendar.getTime()));

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        monthName = monthFormat.format(myCalendar.getTime());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        dayName = dayFormat.format(myCalendar.getTime());
        MenuFetcher menuFetcher = new MenuFetcher(monthName);
        menuFetcher.execute(); // pass the monthName as an argument to the execute method

    }

}


