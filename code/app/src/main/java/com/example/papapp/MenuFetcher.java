package com.example.papapp;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


public class MenuFetcher extends AsyncTask<String, Void, String[][][]> {
    private String monthName;
    private static final String TAG = "MenuFetcher";


    public MenuFetcher(String monthName) {
        this.monthName = monthName;
    }

    @Override
    protected String[][][] doInBackground(String... strings) {
        String[][][] menuData = null;
        // Map each month name to its Greek equivalent
        String greekMonth;
        switch (monthName) {
            case "January":
                greekMonth = "ΙΑΝΟΥΑΡΙΟΣ";
                break;
            case "February":
                greekMonth = "ΦΕΒΡΟΥΑΡΙΟΣ";
                break;
            case "March":
                greekMonth = "ΜΑΡΤΙΟΣ";
                break;
            case "April":
                greekMonth = "ΑΠΡΙΛΙΟΣ";
                break;
            case "May":
                greekMonth = "ΜΑΪΟΣ";
                break;
            case "June":
                greekMonth = "ΙΟΥΝΙΟΣ";
                break;
            case "July":
                greekMonth = "ΙΟΥΛΙΟΣ";
                break;
            case "August":
                greekMonth = "ΑΥΓΟΥΣΤΟΣ";
                break;
            case "September":
                greekMonth = "ΣΕΠΤΕΜΒΡΙΟΣ";
                break;
            case "October":
                greekMonth = "ΟΚΤΩΒΡΙΟΣ";
                break;
            case "November":
                greekMonth = "ΝΟΕΜΒΡΙΟΣ";
                break;
            case "December":
                greekMonth = "ΔΕΚΕΜΒΡΙΟΣ";
                break;
            default:
                greekMonth = "";
        }

        // Adjust the URL accordingly
        String menuUrl = "https://www.upatras.gr/wp-content/uploads/2023/04/ΠΡΟΓΡΑΜΜΑ-ΣΙΤΙΣΗΣ-" + greekMonth.toUpperCase() + "-2023_compressed.pdf";
        try {
            InputStream inputStream = new URL(menuUrl).openStream();
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper stripper = new GreekPDFTextStripper();
            String text = stripper.getText(document);

            // Split the text into pages
            String[] pages = text.split("\f");

            // Initialize the menuData array
            int numWeeks = pages.length / 2;
            menuData = new String[numWeeks][2][];

            // Extract the menu data for each week
            int pageOffset = 0;
            for (int i = 0; i < numWeeks; i++) {
                // Extract the launch and dinner menus for the current week
                String[] launchMenu = extractMenuData(pages[pageOffset]);
                String[] dinnerMenu = extractMenuData(pages[pageOffset + 1]);
                String[][] launchMeals = parseMenu(launchMenu);
                String[][] dinnerMeals = parseMenu(dinnerMenu);
                // Store the meals in the menuData array
                menuData[i][0] = launchMeals;
                menuData[i][1] = dinnerMeals;
                // Increment the page offset to move to the next week
                pageOffset += 2;
            }
            // Close the document and input stream
            document.close();
            inputStream.close();



                // Parse the launch and
        } catch (IOException e) {
            Log.e(TAG, "Error fetching menu data", e);
            return null;
        }
        return  menuData;
    }
}




