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
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }
//private void fetchAnnouncements() throws IOException {
//    InputStream inputStream = getResources().getAssets().open("Announce_scrapper.py");
//    byte[] buffer = new byte[inputStream.available()];
//    inputStream.read(buffer);
//    inputStream.close();
//    // Convert the buffer to a String
//    String script = new String(buffer);
//
//// Run the Python script using the PythonInterpreter class from Jython library
//    PythonInterpreter python = new PythonInterpreter();
//    python.exec(script);
//
//}




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
        // Fetch and display the announcement data
        // Set up ListView and adapter
        announcementsListView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, announcementList);
        announcementsListView.setAdapter(adapter);


        // Set up progress bar
        progressBar = findViewById(R.id.progress_bar);

        // Fetch and display the announcement data
        fetchAnnouncements();

//        announcementsTextView = findViewById(R.id.announcementsTextView);
//        new FetchAnnouncementsTask().execute("https://www.ceid.upatras.gr/el/announcement");


    }

//    private class FetchAnnouncementsTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//            String announcementsData = "";
//
//            try {
//                URL url = new URL(urls[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                InputStream inputStream = urlConnection.getInputStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//                StringBuilder stringBuilder = new StringBuilder();
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    stringBuilder.append(line);
//                }
//
//                announcementsData = stringBuilder.toString();
//
//                reader.close();
//                inputStream.close();
//                urlConnection.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return announcementsData;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // Update the UI with the fetched announcements data
//            announcementsTextView.setText(result);
//        }
//    }

}
