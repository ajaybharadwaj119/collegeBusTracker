package com.akinfopark.colgbustracking;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akinfopark.colgbustracking.Utils.GpsTracker;
import com.akinfopark.colgbustracking.databinding.ActivityMainBinding;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private List<Marker> allMarkers = new ArrayList<>();
    private GpsTracker gpsTracker;
    double latitude = 0.00;
    double longitude = 0.00;
    ActivityMainBinding binding;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("student");

    }

    public void getLocation() {
        gpsTracker = new GpsTracker(MainActivity.this);
        if (gpsTracker.canGetLocation()) {
             latitude = gpsTracker.getLatitude();
             longitude = gpsTracker.getLongitude();
          /*  tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));*/
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //addMarker(new LatLng(8.189463, 77.401532), "Marker 1", "Description 1");
        addMarker(new LatLng(8.188868, 77.408486), "Marker 2", "Description 2");
        addCustomMarker(new LatLng(latitude, longitude), "Bus Driver", "Custom Description");

        LatLng centerPoint = new LatLng(latitude, longitude);
        checkMarkersWithinRadius(centerPoint, 1000);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String employeeName = studentSnapshot.child("employeeName").getValue(String.class);
                    String empLat = studentSnapshot.child("empLat").getValue(String.class);
                    String empLong = studentSnapshot.child("empLong").getValue(String.class);

                    if (employeeName != null && empLat != null && empLong != null) {
                        double latitude = Double.parseDouble(empLat);
                        double longitude = Double.parseDouble(empLong);
                        LatLng location = new LatLng(latitude, longitude);
                        addMarker(location, employeeName, "Description 1");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
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