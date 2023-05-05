package com.example.papapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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
    private AnnouncementAdapter adapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcements);

        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new FetchAnnouncementsTask().execute();
    }
    private class FetchAnnouncementsTask extends AsyncTask<Void, Void, List<Announcements>> {
        @Override
        protected List<Announcements> doInBackground(Void... voids) {
            try {
                return AnnouncementFetcher.fetchAnnouncements();
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<Announcements> announcements) {
            adapter = new AnnouncementAdapter(announcements);
            recyclerView.setAdapter(adapter);
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
