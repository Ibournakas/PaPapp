package com.example.papapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;



public class Announcements extends  AppCompatActivity{
    private SharedPreferences sharedPreferences;
    private RecyclerView announcementsRecyclerView;
    private ProgressBar progressBar;
    private ArrayList<String> announcementList = new ArrayList<>();
    private AnnouncementAdapter adapter;

    private void fetchAnnouncements() {
        progressBar= findViewById(R.id.announcements_progress_bar);
        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Fetch announcements from the website
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    for (int page = 0; page < 5; page++) {

                        String url = "https://www.ceid.upatras.gr/el/announcement?page=" + page;
                        Document document = Jsoup.connect(url).get();

                        Elements announcementNodes = document.select("article.node-announcement");
                        String test = announcementNodes.toString();
                        //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_LONG).show();

                        for (Element node : announcementNodes) {
                            String date = node.select("div.submitted-date").text().trim().replace('\n', ' ');
                            String title = node.select("h2").text().trim();
                            String author = node.select("span.username").text().trim();
                            String category = node.select("div.field-name-field-announcement-category").text().trim().substring(10);
                            String content = node.select("div.content").text().trim();
                            // Add the announcement to the list
                            announcementList.add("Date: " + date + "\nTitle: " + title + "\nAuthor: " + author + "\nCategory: " + category + "\nContent: " + content + "\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Update UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Hide progress bar
                        progressBar.setVisibility(View.GONE);


                        // Initialize the adapter with the fetched announcements
                        adapter = new AnnouncementAdapter(announcementList);

                        // Set the adapter to the RecyclerView
                        announcementsRecyclerView.setAdapter(adapter);

                    }
                });
            }
        }).start();

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
        setContentView(R.layout.announcements);


        // Set up RecyclerView and adapter
        announcementsRecyclerView = findViewById(R.id.announcements_recyclerview);

        announcementsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        announcementsRecyclerView.setAdapter(new AnnouncementAdapter(new ArrayList<>()));


        // Fetch and display the announcement data
        fetchAnnouncements();

    }

}

//   private class FetchAnnouncementsTask extends AsyncTask<Void, Void, ArrayList<String>> {
//
//        @Override
//        protected void onPreExecute() {
//            // Show progress bar
//            ProgressBar progressBar = findViewById(R.id.announcements_progress_bar);
//            progressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected ArrayList<String> doInBackground(Void... voids) {
//            ArrayList<String> announcementList = new ArrayList<>();
//            try {
//                for (int page = 0; page < 5; page++) {
//                    String url = "https://www.ceid.upatras.gr/el/announcement?page=" + page;
//                    Document document = Jsoup.connect(url).get();
//                    Elements announcementNodes = document.select("article.node-announcement");
//
//                    for (Element node : announcementNodes) {
//                        String date = node.select("div.submitted-date").text().trim().replace('\n', ' ');
//                        String title = node.select("h2").text().trim();
//                        String author = node.select("span.username").text().trim();
//                        String category = node.select("div.field-name-field-announcement-category").text().trim().substring(10);
//                        String content = node.select("div.content").text().trim();
//                        // Add the announcement to the list
//                        announcementList.add("Date: " + date + "\nTitle: " + title + "\nAuthor: " + author + "\nCategory: " + category + "\nContent: " + content + "\n");
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return announcementList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<String> announcementList) {
//            // Hide progress bar
//            ProgressBar progressBar = findViewById(R.id.announcements_progress_bar);
//            progressBar.setVisibility(View.GONE);
//
//            // Update the RecyclerView with the announcements
//            RecyclerView recyclerView = findViewById(R.id.announcements_recyclerview);
//            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//            recyclerView.setAdapter(new AnnouncementAdapter(announcementList));
//        }
//    }
//        private void fetchAnnouncements() {
//            new FetchAnnouncementsTask().execute();
//        }