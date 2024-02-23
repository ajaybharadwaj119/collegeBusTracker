package com.akinfopark.colgbustracking;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private List<Marker> allMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        addMarker(new LatLng(8.189463, 77.401532), "Marker 1", "Description 1");
        addMarker(new LatLng(8.188868, 77.408486), "Marker 2", "Description 2");
        addCustomMarker(new LatLng(8.184196, 77.412609), "Bus Driver", "Custom Description");

        LatLng centerPoint = new LatLng(8.184196, 77.412609);
        checkMarkersWithinRadius(centerPoint, 1000);
    }

    private void checkMarkersWithinRadius(LatLng centerPoint, double radius) {
        for (Marker marker : allMarkers) {
            double distance = calculateDistance(centerPoint, marker.getPosition());
            if (distance <= radius) {
                // Marker is within the specified radius
                // You can perform any action or display information about this marker
                Log.d("MarkerWithinRadius", "Marker '" + marker.getTitle() + "' is within the radius.");
                Toast.makeText(this, "Marker '" + marker.getTitle() + "' is within the radius.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private double calculateDistance(LatLng point1, LatLng point2) {
        // Calculate the distance between two LatLng points in meters
        Location location1 = new Location("point1");
        location1.setLatitude(point1.latitude);
        location1.setLongitude(point1.longitude);

        Location location2 = new Location("point2");
        location2.setLatitude(point2.latitude);
        location2.setLongitude(point2.longitude);

        return location1.distanceTo(location2);
    }


    private void addMarker(LatLng position, String title, String snippet) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet);
        Marker marker = mMap.addMarker(markerOptions);
        allMarkers.add(marker);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    private void addCustomMarker(LatLng position, String title, String snippet) {

        BitmapDescriptor customMarkerIcon = fromResource(R.drawable.bus_point);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet)
                .icon(customMarkerIcon);

        CircleOptions circleOptions = new CircleOptions()
                .center(position)
                .radius(1000)
                .strokeColor(Color.RED)
                .strokeWidth(1)
                .fillColor(Color.parseColor("#7ACD7E78"));



        mMap.addMarker(markerOptions);
        mMap.addCircle(circleOptions);
        float zoomLevel = 15.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel));

    }
}