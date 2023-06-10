




package com.example.papapp;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class Train_sched extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.train_sched);
    }
}







//
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.pdf.PdfRenderer;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.ParcelFileDescriptor;
//import android.util.DisplayMetrics;
//import android.view.GestureDetector;
//import android.view.InputDevice;
//import android.view.MotionEvent;
//import android.view.ScaleGestureDetector;
//import android.view.View;
//import android.widget.ImageView;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.DateFormatSymbols;
//import java.util.Calendar;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//
//public class Train_sched extends AppCompatActivity {
//
//    private ImageView pdfImageView;
//    private ScaleGestureDetector scaleGestureDetector;
//    private GestureDetector gestureDetector;
//    private Matrix matrix;
//    private float scaleFactor = 1.0f;
//    private float lastTouchX;
//    private float lastTouchY;
//    private float posX;
//    private float posY;
//    private boolean isDragging = false;
//
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.train_sched);
//
//        pdfImageView = findViewById(R.id.pdfImageView);
//        pdfImageView.setScaleType(ImageView.ScaleType.MATRIX);
//
//        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
//        gestureDetector = new GestureDetector(this, new DoubleTapGestureListener());
//
//        pdfImageView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                scaleGestureDetector.onTouchEvent(event);
//                gestureDetector.onTouchEvent(event);
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        if (scaleFactor > 1.0f) {
//                            isDragging = true;
//                            lastTouchX = event.getX();
//                            lastTouchY = event.getY();
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (isDragging) {
//                            float deltaX = event.getX() - lastTouchX;
//                            float deltaY = event.getY() - lastTouchY;
//                            posX += deltaX;
//                            posY += deltaY;
//
//                            updateMatrix();
//
//                            lastTouchX = event.getX();
//                            lastTouchY = event.getY();
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        isDragging = false;
//                        break;
//                }
//                return true;
//            }
//        });
//        pdfImageView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
//            @Override
//            public boolean onGenericMotion(View v, MotionEvent event) {
//                if (event.isFromSource(InputDevice.SOURCE_CLASS_POINTER)) {
//                    if (event.getAction() == MotionEvent.ACTION_SCROLL) {
//                        float scrollDelta = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
//                        if (scrollDelta != 0.0f) {
//                            float newScaleFactor = scaleFactor + (scrollDelta * 0.1f);
//                            scaleFactor = Math.max(1.0f, Math.min(newScaleFactor, 5.0f));
//                            updateMatrix();
//                        }
//                    }
//                }
//                return false;
//            }
//        });
//
//
//        matrix = new Matrix();
//        String decodedFileName = "";
//        String encodedFileName = "https://www.hellenictrain.gr/sites/default/files/2022-06/%CE%A0%CE%A1%CE%9F%CE%91%CE%A3%CE%A4%CE%99%CE%91%CE%9A%CE%9F%CE%A5%20%CE%A0%CE%91%CE%A4%CE%A1%CE%91%CE%A3%2017.05.2021.pdf";
//
//
//        Uri uri = Uri.parse(encodedFileName);
//        String url = uri.toString();
//
//
//
//        new DownloadPdfTask().execute(url);
//
//
//
//
//    }
//    private String encodeUrl(String url) throws UnsupportedEncodingException {
//        StringBuilder sb = new StringBuilder();
//        for (char c : url.toCharArray()) {
//            if (c > 128) {
//                sb.append(URLEncoder.encode(String.valueOf(c), "UTF-8"));
//            } else {
//                sb.append(c);
//            }
//        }
//        return sb.toString();
//    }
//    private class DownloadPdfTask extends AsyncTask<String, Void, File> {
//        @Override
//        protected File doInBackground(String... urls) {
//            try {
//                String url = urls[0];
//
//                return downloadFile(url);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(File file) {
//            if (file != null) {
//                displayPDFFromFile(file);
//            }
//        }
//    }
//
//    private File downloadFile(String url) throws IOException {
//        URL pdfUrl = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) pdfUrl.openConnection();
//        connection.setRequestMethod("GET");
//        connection.setDoOutput(true);
//        connection.connect();
//
//        File file = new File(getCacheDir(), "downloaded_pdf1.pdf");
//        try (InputStream inputStream = connection.getInputStream();
//             OutputStream outputStream = new FileOutputStream(file)) {
//            byte[] buffer = new byte[4 * 1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//            outputStream.flush();
//        }
//
//        return file;
//    }
//    private void updateMatrix() {
//        matrix.reset();
//        matrix.postScale(scaleFactor, scaleFactor);
//        matrix.postTranslate(posX, posY);
//        pdfImageView.setImageMatrix(matrix);
//    }
//
//    private void displayPDFFromFile(File file) {
//        try (ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)) {
//            if (parcelFileDescriptor != null) {
//                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
//
//                int pageCount = pdfRenderer.getPageCount();
//
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                int displayWidth = displayMetrics.widthPixels;
//                int displayHeight = displayMetrics.heightPixels;
//
//                Bitmap bitmap = Bitmap.createBitmap(displayWidth, displayHeight, Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(bitmap);
//
//                for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
//                    PdfRenderer.Page page = pdfRenderer.openPage(pageIndex);
//
//                    float scale = Math.min((float) displayWidth / page.getWidth(), (float) displayHeight / page.getHeight());
//                    int scaledWidth = Math.round(scale * page.getWidth());
//                    int scaledHeight = Math.round(scale * page.getHeight());
//
//                    Bitmap pageBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
//                    page.render(pageBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//
//                    canvas.drawBitmap(pageBitmap, 0, pageIndex * scaledHeight, null);
//
//                    page.close();
//                }
//
//                pdfRenderer.close();
//
//                pdfImageView.setImageBitmap(bitmap);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector detector) {
//            scaleFactor *= detector.getScaleFactor();
//            scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 5.0f)); // Adjust the min and max scale factor as needed
//
//            matrix.setScale(scaleFactor, scaleFactor);
//            pdfImageView.setImageMatrix(matrix);
//
//            return true;
//        }
//        public boolean onScaleBegin(ScaleGestureDetector detector) {
//            isDragging = false;
//            return super.onScaleBegin(detector);
//        }
//    }
//    private class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
//        private boolean isZoomed = false;
//
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            if (isZoomed) {
//                scaleFactor = 1.0f;
//                isZoomed = false;
//            } else {
//                scaleFactor = 2.0f;
//                isZoomed = true;
//            }
//
//            matrix.setScale(scaleFactor, scaleFactor);
//            pdfImageView.setImageMatrix(matrix);
//
//            return true;
//        }
//
//    }
//}
