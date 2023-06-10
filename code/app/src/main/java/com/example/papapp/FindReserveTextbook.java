package com.example.papapp;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FindReserveTextbook extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SharedPreferences sharedPreferences;
    private TextView bookDetailsTextView;

    // Custom Book class
    private class Book {
        private String name;
        private String author;
        private int year;

        public Book(String name, String author, int year) {
            this.name = name;
            this.author = author;
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public String getAuthor() {
            return author;
        }

        public int getYear() {
            return year;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isDarkModeOn", false)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.find_reserve_textbook);

        Spinner bookSpinner = findViewById(R.id.book_spinner);
        bookSpinner.setOnItemSelectedListener(this);

        List<Book> books = new ArrayList<>();
        books.add(new Book("Introduction to Algorithms", "Thomas H. Cormen", 2009));
        books.add(new Book("Artificial Intelligence: A Modern Approach", "Stuart Russell, Peter Norvig", 2016));
        books.add(new Book("Computer Networking: A Top-Down Approach", "James F. Kurose, Keith W. Ross", 2017));
        books.add(new Book("Database System Concepts", "Abraham Silberschatz, Henry F. Korth, S. Sudarshan", 2019));
        books.add(new Book("Operating System Concepts", "Abraham Silberschatz, Peter B. Galvin, Greg Gagne", 2018));
        books.add(new Book("Compilers: Principles, Techniques, and Tools", "Alfred V. Aho, Monica S. Lam, Ravi Sethi, Jeffrey D. Ullman", 2006));
        books.add(new Book("Computer Organization and Design", "David A. Patterson, John L. Hennessy", 2017));
        books.add(new Book("Computer Graphics: Principles and Practice", "John F. Hughes, Andries van Dam, James D. Foley, Steven K. Feiner, Kurt Akeley", 2013));
        books.add(new Book("Introduction to the Theory of Computation", "Michael Sipser", 2012));
        books.add(new Book("Data Structures and Algorithms", "Michael T. Goodrich, Roberto Tamassia, Michael H. Goldwasser", 2014));

        ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item, books);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSpinner.setAdapter(adapter);

        Button selectButton = findViewById(R.id.select_button);
        Button reserveButton = findViewById(R.id.reserve_button);
        bookDetailsTextView = findViewById(R.id.book_details);

        // Set background colors for the buttons programmatically
        selectButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF4081"))); // Bright Pink
        reserveButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00E676"))); // Bright Green

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book selectedBook = (Book) bookSpinner.getSelectedItem();
                displayBookDetails(selectedBook);
            }
        });

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your reserve button logic here
                Toast.makeText(getApplicationContext(), "Book Reserved Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Do nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    private void displayBookDetails(Book book) {
        String details = "Book Details:\n\n"
                + "Name: " + book.getName() + "\n"
                + "Author: " + book.getAuthor() + "\n"
                + "Year: " + book.getYear();

        bookDetailsTextView.setText(details);
        bookDetailsTextView.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), "Selected Book: " + book.getName(), Toast.LENGTH_SHORT).show();
    }
}
