package com.example.papapp;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;

public interface OnMapReadyCallBack {
    void onMapReady(@NonNull GoogleMap googleMap);
}
