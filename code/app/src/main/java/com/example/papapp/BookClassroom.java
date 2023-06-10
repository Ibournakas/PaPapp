package com.example.papapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.app.Dialog;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class BookClassroom extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;
    private EditText searchEditText;
    private List<Classroom> classrooms;
    private List<Marker> classroomMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_classroom);
        // Initialize the list of classrooms
        setupClassrooms();

        // Initialize the search EditText
        searchEditText = findViewById(R.id.searchEditText);

        // Find the zoom buttons by their IDs
        zoomInButton = findViewById(R.id.zoomInButton);
        zoomOutButton = findViewById(R.id.zoomOutButton);

        // Set click listeners for the zoom buttons
        zoomInButton.setOnClickListener(v -> zoomIn());
        zoomOutButton.setOnClickListener(v -> zoomOut());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapActivity);
        mapFragment.getMapAsync(this);
    }

    // Zoom in by increasing the camera zoom level
    private void zoomIn() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    // Zoom out by decreasing the camera zoom level
    private void zoomOut() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (isLocationPermissionGranted()) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestLocationPermission();
        }

        // Add markers for the classrooms
        addClassroomMarkers();

        // Set the default camera position to Classroom B
        LatLng classroomBLocation = new LatLng(classrooms.get(0).getLatitude(), classrooms.get(0).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(classroomBLocation, 15.0f));

        // Set marker click listener
        mMap.setOnMarkerClickListener(marker -> {
            // Create a dialog for the popup window
            Dialog dialog = new Dialog(BookClassroom.this);
            dialog.setContentView(R.layout.popup_window);

            // Get references to the views in the popup window
            TextView popupTitle = dialog.findViewById(R.id.popupTitle);
            TextView popupMessage = dialog.findViewById(R.id.popupMessage);
            Button popupButton = dialog.findViewById(R.id.popupButton);

            // Set the title and message
            popupTitle.setText(marker.getTitle());
            popupMessage.setText("This is a classroom. Choose the booking details below.");

            // Set the button click listener
            popupButton.setOnClickListener(v -> {
                // Perform an action when the button is clicked
                // Show a toast message of successful completion
                Toast.makeText(BookClassroom.this, "Classroom booked successfully!", Toast.LENGTH_SHORT).show();
                // You can customize this part to perform any desired action

                // Dismiss the dialog
                dialog.dismiss();
            });

            // Show the dialog
            dialog.show();

            // Return false to allow default marker click behavior (open info window)
            return false;
        });
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void searchClassroom() {
        String classroomName = searchEditText.getText().toString().trim();
        // Perform a search for the classroom by its name
        LatLng classroomLocation = getLatLngForClassroom(classroomName);
        if (classroomLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(classroomLocation, 15.0f));
        } else {
            // Handle case when classroom location is not found
        }
    }

    // Method to fetch the LatLng for the classroom based on its name
    private LatLng getLatLngForClassroom(String classroomName) {
        // TODO: Implement your logic to fetch the LatLng for the classroom from a database or API
        // Return the LatLng object representing the classroom's location if found, or null otherwise
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location-related functionality
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                // Permission denied, disable location-related functionality or handle accordingly
            }
        }
    }

    private void setupClassrooms() {
        classrooms = new ArrayList<>();
        classroomMarkers = new ArrayList<>();

        Classroom classroom1 = new Classroom("Classroom B", 38.29069979600452, 21.79474702792678);
        Classroom classroom2 = new Classroom("Classroom C", 38.2902825591203, 21.794762663035996);
        Classroom classroom3 = new Classroom("Classroom D1", 38.29085932712043, 21.7954506078418);
        Classroom classroom4 = new Classroom("Classroom D2", 38.290887960873334, 21.79527340993727);

        classrooms.add(classroom1);
        classrooms.add(classroom2);
        classrooms.add(classroom3);
        classrooms.add(classroom4);

        Spinner classroomSpinner = findViewById(R.id.classroomSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getClassroomNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroomSpinner.setAdapter(adapter);

        classroomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedClassroomName = parent.getItemAtPosition(position).toString();
                LatLng classroomLocation = getLatLngForClassroom(selectedClassroomName);
                if (classroomLocation != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(classroomLocation, 15.0f));
                } else {
                    // Handle case when classroom location is not found
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private List<String> getClassroomNames() {
        List<String> classroomNames = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            classroomNames.add(classroom.getName());
        }
        return classroomNames;
    }

    private void addClassroomMarkers() {
        for (Classroom classroom : classrooms) {
            LatLng classroomLocation = new LatLng(classroom.getLatitude(), classroom.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(classroomLocation).title(classroom.getName()));
            classroomMarkers.add(marker);
        }
    }

    private static class Classroom {
        private String name;
        private double latitude;
        private double longitude;

        Classroom(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        String getName() {
            return name;
        }

        double getLatitude() {
            return latitude;
        }

        double getLongitude() {
            return longitude;
        }
    }
}
