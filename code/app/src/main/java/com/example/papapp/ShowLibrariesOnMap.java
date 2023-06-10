package com.example.papapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowLibrariesOnMap extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private ImageButton zoomInButton;
    private ImageButton zoomOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

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
        LatLng uopLibrary = new LatLng(38.289930759335896, 21.79134348313736);
        mMap.addMarker(new MarkerOptions().position(uopLibrary).title("The main Library of the University of Patras"));

        LatLng medicineLibrary = new LatLng(38.294006363076626, 21.79330686009443);
        mMap.addMarker(new MarkerOptions().position(medicineLibrary).title("Library of the Medical School"));

        LatLng architectureLibrary = new LatLng(38.286262980192085, 21.784081784272345);
        mMap.addMarker(new MarkerOptions().position(architectureLibrary).title("Library of the Architecture School"));

        LatLng philosophyLibrary = new LatLng(38.28451457028744, 21.78604511190198);
        mMap.addMarker(new MarkerOptions().position(philosophyLibrary).title("Library of the Philosophy School"));

        // Move the camera to a specific location and set zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uopLibrary, 15.0f)); // Adjust the zoom level here
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
}