package com.example.papapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AnnouncementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private com.example.papapp.Announcement_Services.AnnouncementAdapter adapter;
    private ProgressBar mProgressBar;
    private SharedPreferences sharedPreferences;

    private int currentPage = 0;
    private int lastPage = 2;

    private Button nextPageButton;


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
        setContentView(R.layout.announcements);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Add an OnClickListener to the next button
        nextPageButton = findViewById(R.id.next_button);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < lastPage) {
                    currentPage++;
                    loadAnnouncements(currentPage);
                }
            }
        });


        loadAnnouncements(currentPage);
    }
    private void loadAnnouncements(int page) {
        new FetchAnnouncementsTask().execute(page);
    }
    private class FetchAnnouncementsTask extends AsyncTask<Integer, Void, List<com.example.papapp.Announcement_Services.Announcements>> {
        @Override
        protected List<com.example.papapp.Announcement_Services.Announcements> doInBackground(Integer... integers) {
            int page = integers[0];
            try {
                return com.example.papapp.Announcement_Services.AnnouncementFetcher.fetchAnnouncements(page);
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<com.example.papapp.Announcement_Services.Announcements> announcements) {
            adapter = new com.example.papapp.Announcement_Services.AnnouncementAdapter(announcements);
            recyclerView.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);

            if (currentPage < lastPage) {
                nextPageButton.setEnabled(true);
            } else {
                nextPageButton.setVisibility(View.GONE);
                Toast.makeText(AnnouncementActivity.this, "You've reached the max amount of announcements (60).", Toast.LENGTH_SHORT).show();

            }
        }

    }




}
