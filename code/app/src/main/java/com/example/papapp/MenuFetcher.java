//package com.example.papapp;
//
//import android.os.AsyncTask;
//import android.util.Log;
//import java.io.IOException;
//import java.io.InputStream;
//
//import java.net.URL;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.font.PDFont;
//import org.apache.pdfbox.pdmodel.font.PDType0Font;
//import android.content.Context;
//import android.content.res.AssetManager;
//
//
//
//
//public class MenuFetcher extends AsyncTask<String, Void, String[][][][]> {
//    private String monthName;
//    private static final String TAG = "MenuFetcher";
//    private PDFont greekFont;
//    private Context context;
//
//    public MenuFetcher(String monthName) {
//
//        this.context = context;
//        this.monthName = monthName;
//        // Load the Greek font
////        try {
////            InputStream fontInputStream = getClass().getResourceAsStream("/raw/arial_gr.ttf");
////            if (fontInputStream == null) {
////                Log.e(TAG, "Font file not found");
////            } else {
////                PDDocument tempDoc = new PDDocument();
////                greekFont = PDType0Font.load(tempDoc, fontInputStream);
////                tempDoc.close();
////            }
////        } catch (IOException e) {
////            Log.e(TAG, "Error loading Greek font", e);
////        }
//    }
//
//
//    @Override
//    protected String[][][][] doInBackground(String... strings) {
//        String[][][][] menuData = null;
//        // Initialize the PDDocument object
//        PDDocument document = null;
//        // Map each month name to its Greek equivalent
//        String greekMonth;
//        switch (monthName) {
//            case "January":
//                greekMonth = "ΙΑΝΟΥΑΡΙΟΣ";
//                break;
//            case "February":
//                greekMonth = "ΦΕΒΡΟΥΑΡΙΟΣ";
//                break;
//            case "March":
//                greekMonth = "ΜΑΡΤΙΟΣ";
//                break;
//            case "April":
//                greekMonth = "ΑΠΡΙΛΙΟΣ";
//                break;
//            case "May":
//                greekMonth = "ΜΑΪΟΣ";
//                break;
//            case "June":
//                greekMonth = "ΙΟΥΝΙΟΣ";
//                break;
//            case "July":
//                greekMonth = "ΙΟΥΛΙΟΣ";
//                break;
//            case "August":
//                greekMonth = "ΑΥΓΟΥΣΤΟΣ";
//                break;
//            case "September":
//                greekMonth = "ΣΕΠΤΕΜΒΡΙΟΣ";
//                break;
//            case "October":
//                greekMonth = "ΟΚΤΩΒΡΙΟΣ";
//                break;
//            case "November":
//                greekMonth = "ΝΟΕΜΒΡΙΟΣ";
//                break;
//            case "December":
//                greekMonth = "ΔΕΚΕΜΒΡΙΟΣ";
//                break;
//            default:
//                greekMonth = "";
//        }
//
//        // Adjust the URL accordingly
//        String menuUrl = "https://www.upatras.gr/wp-content/uploads/2023/04/ΠΡΟΓΡΑΜΜΑ-ΣΙΤΙΣΗΣ-" + greekMonth.toUpperCase() + "-2023_compressed.pdf";
//        try {
//            InputStream inputStream = new URL(menuUrl).openStream();
//            document = PDDocument.load(inputStream);
//
//            // Load the Greek font
//            AssetManager assetManager = context.getAssets();
//            InputStream fontInputStream = assetManager.open("arial_gr.ttf");
//            PDDocument tempDoc = new PDDocument();
//            PDType0Font greekFont = PDType0Font.load(tempDoc, fontInputStream);
//            tempDoc.close();
//
//
//
//            GreekPDFTextStripper stripper = new GreekPDFTextStripper(greekFont);
//            String text =stripper.getText(document);
//
//            // Split the text into pages
//            String[] pages = text.split("\f");
//
//            // Initialize the menuData array
//            int numWeeks = pages.length / 2;
//            menuData = new String[numWeeks][2][][];
//
//            // Extract the menu data for each week
//            int pageOffset = 0;
//            for (int i = 0; i < numWeeks; i++) {
//                // Extract the launch and dinner menus for the current week
//                String[] launchMenu = extractMenuData(pages[pageOffset]);
//                String[] dinnerMenu = extractMenuData(pages[pageOffset + 1]);
//                // Parse the launch and dinner menus for the current week
//                String[][] launchMeals = parseMenu(launchMenu);
//                String[][] dinnerMeals = parseMenu(dinnerMenu);
//
//                // Store the meals in the menuData array
//                menuData[i][0] = launchMeals;
//                menuData[i][1] = dinnerMeals;
//
//                // Increment the page offset to move to the next week
//                pageOffset += 2;
//            }
//            // Close the document and input stream
//            document.close();
//            inputStream.close();
//
//
//
//                // Parse the launch and
//        } catch (IOException e) {
//            Log.e(TAG, "Error fetching menu data", e);
//            return null;
//        }
//        return  menuData;
//    }
//    private String[] extractMenuData(String page) {
//        // Extract the menu items from the page
//        String[] menuItems = page.trim().split("\n");
//        // Remove empty lines and leading/trailing whitespaces
//        for (int i = 0; i < menuItems.length; i++) {
//            menuItems[i] = menuItems[i].trim();
//        }
//        return menuItems;
//    }
//
//    private String[][] parseMenu(String[] menuItems) {
//        String[][] meals = new String[menuItems.length][];
//        for (int i = 0; i < menuItems.length; i++) {
//            String[] mealItems = menuItems[i].split(",");
//            for (int j = 0; j < mealItems.length; j++) {
//                mealItems[j] = mealItems[j].trim();
//            }
//            meals[i] = mealItems;
//        }
//        return meals;
//    }
//
//
//
//
//}
//
//
//
//
