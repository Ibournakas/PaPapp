package com.example.papapp;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MenuFetcher extends AsyncTask<Void, Void, String> {

    private static final String TAG = "MenuFetcher";

    @Override
    protected String doInBackground(Void... voids) {
        String menuUrl = "https://www.upatras.gr/wp-content/uploads/2023/04/ΠΡΟΓΡΑΜΜΑ-ΣΙΤΙΣΗΣ-ΜΑΪΟΣ-2023_compressed.pdf";
        try {
            URL url = new URL(menuUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching menu data", e);
            return null;
        }
    }
}
