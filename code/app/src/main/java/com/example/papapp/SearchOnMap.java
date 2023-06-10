package com.example.papapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class SearchOnMap extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapActivity);
        mapFragment.getMapAsync(this);

        // Find the zoom buttons by their IDs
        ImageButton zoomInButton = findViewById(R.id.zoomInButton);
        ImageButton zoomOutButton = findViewById(R.id.zoomOutButton);

        // Set click listeners for the zoom buttons
        zoomInButton.setOnClickListener(v -> zoomIn());
        zoomOutButton.setOnClickListener(v -> zoomOut());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (isLocationPermissionGranted()) {
            mMap.setMyLocationEnabled(true);
        } else {
            requestLocationPermission();
        }
        LatLng Cafeteria = new LatLng(38.28623675080068, 21.789363045555078);
        mMap.addMarker(new MarkerOptions().position(Cafeteria).title("The University Cafeteria - Restaurant"));

        LatLng Museum = new LatLng(38.28787963314966, 21.784635601845473);
        mMap.addMarker(new MarkerOptions().position(Museum).title("Museum of Science and Technology"));

        LatLng Administration = new LatLng(38.286031387920715, 21.78700834546132);
        mMap.addMarker(new MarkerOptions().position(Administration).title("Administration Building, Rectorate of the University of Patras"));

        LatLng Church  = new LatLng(38.28674678231724, 21.78817734239628);
        mMap.addMarker(new MarkerOptions().position(Church ).title("Church of the Three Hierarchs"));

        LatLng ConferenceCenter = new LatLng(38.29018258115186, 21.786331982602317);
        mMap.addMarker(new MarkerOptions().position(ConferenceCenter).title("Conference & Cultural Center"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Administration, 15.0f)); // Adjust the zoom level here

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("Administration of the University of Patras")) {
                    Toast.makeText(SearchOnMap.this, "Custom info for Administration of the University of Patras", Toast.LENGTH_SHORT).show();
                } else {
                    showCustomInfoDialog(marker);
                }
                return false;
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission denied, handle accordingly or disable location-related functionality
            }
        }
    }

    private void showCustomInfoDialog(Marker marker) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.building_info_dialog, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
        TextView infoTextView = dialogView.findViewById(R.id.infoTextView);
        Button linkButton = dialogView.findViewById(R.id.linkButton);

        titleTextView.setText(marker.getTitle());
        infoTextView.setText("Custom info for " + marker.getTitle());

        // Assign URLs to markers
        String url;
        if (marker.getTitle().equals("Administration of the University of Patras")) {
            url = "https://www.upatras.gr/en/";
        } else if (marker.getTitle().equals("The University Cafeteria - Restaurant")) {
            url = "https://www.upatras.gr/foitites/foititiki-merimna/sitisi/";
        } else if (marker.getTitle().equals("Museum of Science and Technology")) {
            url = "http://stmuseum.upatras.gr/index.php/en/";
        } else if (marker.getTitle().equals("Administration Building, Rectorate of the University of Patras")) {
            url = "https://www.upatras.gr/en/university/administration/";
        } else if (marker.getTitle().equals("Church")) {
            url = "https://www.example.com/url5";
        } else if (marker.getTitle().equals("ConferenceCenter")) {
            url = "http://www.confer.upatras.gr/";
        } else {
            url = ""; // Set a default URL or handle other cases as needed
        }

        final String finalUrl = url; // Declare final to access inside OnClickListener
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!finalUrl.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                    startActivity(intent);
                } else {
                    String message;
                    if (marker.getTitle().equals("Church")) {
                        message = "No URL available for this marker.";
                    } else {
                        message = "URL not available";
                    }
                    Toast.makeText(SearchOnMap.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });


        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void zoomIn() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    private void zoomOut() {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }
}