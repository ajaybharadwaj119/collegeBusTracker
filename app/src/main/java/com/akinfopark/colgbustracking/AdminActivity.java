package com.akinfopark.colgbustracking;

import static android.content.ContentValues.TAG;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.akinfopark.colgbustracking.Utils.DialogUtils;
import com.akinfopark.colgbustracking.databinding.ActivityAdminBinding;
import com.akinfopark.colgbustracking.databinding.ActivityMainBinding;
import com.akinfopark.colgbustracking.databinding.DialogYesNoBinding;
import com.akinfopark.colgbustracking.loginReg.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity implements OnMapReadyCallback {
    ActivityMainBinding binding;
    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private PlacesClient placesClient;
    private Polyline currentPolyline;
    DialogYesNoBinding yesNoBinding;
    AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        yesNoBinding = DialogYesNoBinding.inflate(getLayoutInflater());
        exitDialog = DialogUtils.getCustomAlertDialog(AdminActivity.this, yesNoBinding.getRoot());
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        databaseReference = FirebaseDatabase.getInstance().getReference("driver");
        mapFragment.getMapAsync(this);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    String drivBusNumber = driverSnapshot.child("drivBusNumber").getValue(String.class);
                    String drivName = driverSnapshot.child("drivName").getValue(String.class);
                    String drivNumber = driverSnapshot.child("drivNumber").getValue(String.class);
                    Log.d(TAG, "Driver Bus Number: " + drivBusNumber);
                    Log.d(TAG, "Driver Name: " + drivName);
                    Log.d(TAG, "Driver Number: " + drivNumber);
                    // You can retrieve other data in a similar way

                    double latitude = (double) driverSnapshot.child("drivLat").getValue();
                    double longitude = (double) driverSnapshot.child("drivLong").getValue();

                    addCustomMarker(new LatLng(latitude, longitude), drivBusNumber, drivName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value for drivers", databaseError.toException());
            }
        });


        binding.imgExit.setOnClickListener(v -> {
            exitDialog.show();
        });

        yesNoBinding.textViewMessage.setText("Are you sure you want to Exit?");
        yesNoBinding.buttonYes.setOnClickListener(v -> {
            exitDialog.dismiss();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        yesNoBinding.buttonNo.setOnClickListener(v ->

        {
            exitDialog.dismiss();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void addCustomMarker(LatLng position, String title, String snippet) {

        BitmapDescriptor customMarkerIcon = fromResource(R.drawable.bus_point);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet)
                .icon(customMarkerIcon);

        mMap.addMarker(markerOptions);
        float zoomLevel = 4.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel));

    }

}