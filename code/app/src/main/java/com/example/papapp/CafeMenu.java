package com.example.papapp;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CafeMenu extends AppCompatActivity {
    private ImageView pdfImageView;
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private Matrix matrix;
    private float scaleFactor = 1.0f;
    private float lastTouchX;
    private float lastTouchY;
    private float posX;
    private float posY;
    private boolean isDragging = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafemenu);

        pdfImageView = findViewById(R.id.pdfImageView);
        pdfImageView.setScaleType(ImageView.ScaleType.MATRIX);


        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
        gestureDetector = new GestureDetector(this, new DoubleTapGestureListener());

        pdfImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                gestureDetector.onTouchEvent(event);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (scaleFactor > 1.0f) {
                            isDragging = true;
                            lastTouchX = event.getX();
                            lastTouchY = event.getY();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDragging) {
                            float deltaX = event.getX() - lastTouchX;
                            float deltaY = event.getY() - lastTouchY;
                            posX += deltaX;
                            posY += deltaY;

                            updateMatrix();

                            lastTouchX = event.getX();
                            lastTouchY = event.getY();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        isDragging = false;
                        break;
                }
                return true;
            }
        });
        pdfImageView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                if (event.isFromSource(InputDevice.SOURCE_CLASS_POINTER)) {
                    if (event.getAction() == MotionEvent.ACTION_SCROLL) {
                        float scrollDelta = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
                        if (scrollDelta != 0.0f) {
                            float newScaleFactor = scaleFactor + (scrollDelta * 0.1f);
                            scaleFactor = Math.max(1.0f, Math.min(newScaleFactor, 5.0f));
                            updateMatrix();
                        }
                    }
                }
                return false;
            }
        });


        matrix = new Matrix();
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH); // Note: January is represented by 0
        String monthString = new DateFormatSymbols().getMonths()[month];
        String greekMonth;
        switch (monthString) {
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
        String menuUrl = "https://www.upatras.gr/wp-content/uploads/2023/04/ΠΡΟΓΡΑΜΜΑ-ΣΙΤΙΣΗΣ-" + greekMonth.toUpperCase() + "-2023_compressed.pdf";
        

        new DownloadPdfTask().execute(menuUrl);
    }

    private class DownloadPdfTask extends AsyncTask<String, Void, File> {
        @Override
        protected File doInBackground(String... urls) {
            try {
                String url = urls[0];
                return downloadFile(url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                displayPDFFromFile(file);
            }
        }
    }

    private File downloadFile(String url) throws IOException {
        URL pdfUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) pdfUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.connect();

        File file = new File(getCacheDir(), "downloaded_pdf.pdf");
        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }

        return file;
    }
    private void updateMatrix() {
        matrix.reset();
        matrix.postScale(scaleFactor, scaleFactor);
        matrix.postTranslate(posX, posY);
        pdfImageView.setImageMatrix(matrix);
    }

    private void displayPDFFromFile(File file) {
        try (ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)) {
            if (parcelFileDescriptor != null) {
                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                PdfRenderer.Page page = pdfRenderer.openPage(0);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int displayWidth = displayMetrics.widthPixels;
                int displayHeight = displayMetrics.heightPixels;

                float scale = Math.min((float) displayWidth / page.getWidth(), (float) displayHeight / page.getHeight());
                int scaledWidth = Math.round(scale * page.getWidth());
                int scaledHeight = Math.round(scale * page.getHeight());

                Bitmap bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                pdfImageView.setImageBitmap(bitmap);

                page.close();
                pdfRenderer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 5.0f)); // Adjust the min and max scale factor as needed

            matrix.setScale(scaleFactor, scaleFactor);
            pdfImageView.setImageMatrix(matrix);

            return true;
        }
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            isDragging = false;
            return super.onScaleBegin(detector);
        }
    }
    private class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean isZoomed = false;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (isZoomed) {
                scaleFactor = 1.0f;
                isZoomed = false;
            } else {
                scaleFactor = 2.0f;
                isZoomed = true;
            }

            matrix.setScale(scaleFactor, scaleFactor);
            pdfImageView.setImageMatrix(matrix);

            return true;
        }

    }



}

















//package com.example.papapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.DatePickerDialog;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.papapp.Announcement_Services.Announcements;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//
//import com.github.barteksc.pdfviewer.PDFView;
//
//public class CafeMenu  extends AppCompatActivity {
//    private SharedPreferences sharedPreferences;
//    final Calendar myCalendar= Calendar.getInstance();
//    private EditText dateText;
//    private String monthName;
//    private String dayName;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
//            setTheme(R.style.Theme_Dark);
//        } else {
//            setTheme(R.style.Theme_Light);
//        }
//        setContentView(R.layout.cafemenu);
//
//        DatePickerDialog.OnDateSetListener datePickerListener = (view, year, month, dayOfMonth) -> {
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH,month);
//            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//            try {
//                updateDateLabel();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
////            Log.d("CafeMenu", "Selected day name: " + dayName);
////
////            // use dayStr and monthStr variables as required
////            Log.d("CafeMenu", "Selected month: " + monthName);
//        };
//
//
//        dateText = findViewById(R.id.date_picker);
//        dateText.setOnClickListener(v -> new DatePickerDialog(CafeMenu.this,datePickerListener,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show());
//
//        Spinner spinner = findViewById(R.id.menu_spinner);
//        String[] options = {"Day", "Week"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
//        spinner.setAdapter(adapter);
//        spinner.setSelection(0); // Set "Day" as the default selection
//
//
//
//
//
//    }
//    private void updateDateLabel() throws IOException {
//        if(dateText.getError() != null){
//            dateText.setError(null);
//        }
//        String myFormat="dd-MM-yy";
//        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.getDefault());
//        dateText.setText(dateFormat.format(myCalendar.getTime()));
//
//        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
//        monthName = monthFormat.format(myCalendar.getTime());
//        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
//        dayName = dayFormat.format(myCalendar.getTime());
//        MenuFetcher menuFetcher = new MenuFetcher(monthName);
//        menuFetcher.execute(); // pass the monthName as an argument to the execute method
//
//    }
//
//}
//
//
