package com.example.papapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//
//import org.python.util.PythonInterpreter;
//

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



import java.io.IOException;


public class Announcements extends  AppCompatActivity{
    private SharedPreferences sharedPreferences;
    private ListView announcementsListView;
    private ProgressBar progressBar;
    private ArrayList<String> announcementList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private void fetchAnnouncements() {
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


                        // Update the ListView with the announcements
                        adapter.notifyDataSetInvalidated();
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


        // Set up ListView and adapter
        announcementsListView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, announcementList);
        announcementsListView.setAdapter(adapter);


        // Set up progress bar
        progressBar = findViewById(R.id.progress_bar);

        // Fetch and display the announcement data
        fetchAnnouncements();
//        Log.d("Test",announcementList.get(0).substring(1));





    }

}
