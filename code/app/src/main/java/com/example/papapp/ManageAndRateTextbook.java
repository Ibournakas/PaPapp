package com.example.papapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageAndRateTextbook extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<Book> borrowedBooks;
    private BorrowedBooksAdapter adapter;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setTheme(sharedPreferences.getBoolean("isDarkModeOn", false) ? R.style.Theme_Dark : R.style.Theme_Light);
        setContentView(R.layout.manage_and_rate);

        borrowedBooks = new ArrayList<>();

        loadBooksFromSharedPreferences();
        loadRatingsAndReviews();

        ListView listView = findViewById(R.id.listView);
        adapter = new BorrowedBooksAdapter(borrowedBooks, this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            showRatingPopup(borrowedBooks.get(position), view);
        });

        Button addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(v -> showAddBookDialog());
    }

    private void showAddBookDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a New Book");

        final EditText bookTitleEditText = new EditText(this);
        bookTitleEditText.setHint("Enter book title");
        builder.setView(bookTitleEditText);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String bookTitle = bookTitleEditText.getText().toString();
            if (!bookTitle.isEmpty()) {
                Book newBook = new Book(bookTitle);
                borrowedBooks.add(newBook);
                saveBooksToSharedPreferences();
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showRatingPopup(Book book, View view) {
        if (book.getTitle().equals("Book 4")) {
            // Show a pop-up for Book 4
            // You can customize the pop-up for Book 4 with appropriate message or actions
            Toast.makeText(this, "You are restricted from rating Book 4.\nBook returned in bad condition...", Toast.LENGTH_SHORT).show();
            return;
        } else if (book.getTitle().equals("Book 5")) {
            // Show a pop-up for Book 5
            // You can customize the pop-up for Book 5 with appropriate message or actions
            Toast.makeText(this, "You are restricted from rating Book 5.\nBook returned after its due date...", Toast.LENGTH_SHORT).show();
            return;
        }

        // For other books, show the regular rating pop-up
        LinearLayout ratingPopup = findViewById(R.id.ratingPopup);
        ratingPopup.setVisibility(View.VISIBLE);

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        EditText reviewEditText = findViewById(R.id.reviewEditText);
        Button submitButton = findViewById(R.id.submitButton);
        Button clearRatingButton = findViewById(R.id.clearRatingButton);

        // Retrieve the last saved rating and review for the book
        float lastRating = book.getRating();
        String lastReview = book.getReview();

        // Set the retrieved rating and review in the pop-up fields
        ratingBar.setRating(lastRating);
        reviewEditText.setText(lastReview);

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String review = reviewEditText.getText().toString();

            book.setRating(rating);
            book.setReview(review);

            saveRatingAndReview(book);

            ratingPopup.setVisibility(View.GONE);

            Toast.makeText(ManageAndRateTextbook.this, "Rating submitted", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged(); // Update the adapter to reflect changes
        });

        clearRatingButton.setOnClickListener(v -> {
            ratingBar.setRating(0.0f);
            reviewEditText.setText(null);

            // Automatically submit the change and close the pop-up
            submitButton.performClick();
        });
    }


    private void saveBooksToSharedPreferences() {
        JSONArray jsonArray = new JSONArray();
        for (Book book : borrowedBooks) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("title", book.getTitle());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString("borrowedBooks", jsonArray.toString());
        editor.apply();
    }

    private void loadBooksFromSharedPreferences() {
        String jsonString = sharedPreferences.getString("borrowedBooks", null);
        if (jsonString != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    borrowedBooks.add(new Book(title));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveRatingAndReview(Book book) {
        editor.putFloat(book.getTitle() + "_rating", book.getRating());
        editor.putString(book.getTitle() + "_review", book.getReview());
        editor.apply();
    }

    private void loadRatingsAndReviews() {
        for (Book book : borrowedBooks) {
            float rating = sharedPreferences.getFloat(book.getTitle() + "_rating", 0);
            String review = sharedPreferences.getString(book.getTitle() + "_review", "");

            book.setRating(rating);
            book.setReview(review);
        }
    }

    private static class Book {
        private String title;
        private float rating;
        private String review;

        public Book(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }
    }

    private class BorrowedBooksAdapter extends BaseAdapter {
        private List<Book> borrowedBooks;
        private Context context;

        public BorrowedBooksAdapter(List<Book> borrowedBooks, Context context) {
            this.borrowedBooks = borrowedBooks;
            this.context = context;
        }

        @Override
        public int getCount() {
            return borrowedBooks.size();
        }

        @Override
        public Object getItem(int position) {
            return borrowedBooks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.book_item_layout, parent, false);
            }

            TextView titleTextView = convertView.findViewById(R.id.titleTextView);
            titleTextView.setText(borrowedBooks.get(position).getTitle());

            TextView ratingTextView = convertView.findViewById(R.id.ratingTextView);
            updateRatingText(position, ratingTextView);

            return convertView;
        }

        private void updateRatingText(int position, TextView ratingTextView) {
            String ratingText = "  Rating: " + borrowedBooks.get(position).getRating();
            String reviewText = "Review: " + borrowedBooks.get(position).getReview();

            SpannableString spannableString = new SpannableString(ratingText);
            Drawable starDrawable = ContextCompat.getDrawable(context, android.R.drawable.star_big_on);
            if (starDrawable != null) {
                int starSize = ratingTextView.getLineHeight(); // Adjust the star size based on the line height of the TextView
                starDrawable.setBounds(0, 0, starSize, starSize);
                starDrawable.setTint(Color.rgb(200, 160, 0)); // Set a darker shade of yellow (R:200, G:160, B:0)
                ImageSpan imageSpan = new ImageSpan(starDrawable, ImageSpan.ALIGN_BOTTOM);
                spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }

            ratingTextView.setText(spannableString);
            ratingTextView.append("\n" + reviewText);
        }

    }
}