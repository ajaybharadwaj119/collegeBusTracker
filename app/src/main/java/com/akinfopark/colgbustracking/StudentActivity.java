package com.akinfopark.colgbustracking;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.akinfopark.colgbustracking.Utils.CommonFunctions;
import com.akinfopark.colgbustracking.Utils.DialogUtils;
import com.akinfopark.colgbustracking.Utils.GpsTracker;
import com.akinfopark.colgbustracking.Utils.MyPrefs;
import com.akinfopark.colgbustracking.databinding.ActivityDriverRegBinding;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private List<Marker> allMarkers = new ArrayList<>();
    private GpsTracker gpsTracker;
    double latitude = 0.00;
    double longitude = 0.00;
    double colgLat = 0.0, colgLong = 0.0;
    String busNum = "";
    Activity activity;
    ActivityMainBinding binding;
    private DatabaseReference databaseReference;
    private LatLng originLatLng = new LatLng(8.166098, 77.397560); // Starting point
    private LatLng destinationLatLng = new LatLng(8.202119, 77.449436); // Ending point
    private PlacesClient placesClient;
    private Polyline currentPolyline;
    DialogYesNoBinding yesNoBinding;
    AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        yesNoBinding = DialogYesNoBinding.inflate(getLayoutInflater());
        exitDialog = DialogUtils.getCustomAlertDialog(StudentActivity.this, yesNoBinding.getRoot());
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bundle bundle = CommonFunctions.getBundle(StudentActivity.this);

        // if (!bundle.getString("email").equalsIgnoreCase("")) {
        String bundValue = MyPrefs.getInstance(getApplicationContext()).getString("email");
        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference().child("student");

// Query the database to find the student with empEmail "ajay@akinfopark.co"
        Query query = studentsRef.orderByChild("empEmail").equalTo(bundValue);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the results
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    // Retrieve the empPassword value
                    busNum = studentSnapshot.child("empBusNum").getValue(String.class);
                    DatabaseReference driversRef = FirebaseDatabase.getInstance().getReference().child("driver");

// Query the database to find the driver with drivBusNumber "D1"
                    Query queryBus = driversRef.orderByChild("drivBusNumber").equalTo(busNum);
                    queryBus.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Iterate through the results
                            for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                                // Retrieve the drivName value
                                String drivName = driverSnapshot.child("drivName").getValue(String.class);
                                double lati = (double) driverSnapshot.child("drivLat").getValue();
                                double longi = (double) driverSnapshot.child("drivLong").getValue();
                                // Now you can use the drivName as needed
                                Log.d("DriverName", "The driver name is: " + drivName + "  " + lati + "  " + longi);

                                addCustomMarker(new LatLng(lati, longi), drivName, "");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
        //  }


        // Assuming you have a DatabaseReference reference to your "driver" node


        binding.tvTitle.setText("Student Login");


        // Initialize Places SDK
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //  mapFragment.getMapAsync(this);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("student");


        mapFragment.getMapAsync(this);


        binding.imgExit.setOnClickListener(v -> {
            exitDialog.show();
        });
        yesNoBinding.textViewMessage.setText("Are you sure you want to Exit?");
        yesNoBinding.buttonYes.setOnClickListener(v -> {
            exitDialog.dismiss();
            MyPrefs.getInstance(getApplicationContext()).putString("login", "");
            Intent intent = new Intent(StudentActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        yesNoBinding.buttonNo.setOnClickListener(v -> {
            exitDialog.dismiss();
        });

    }


    public void getLocation() {
        gpsTracker = new GpsTracker(StudentActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
          /*tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));*/
        } else {
            gpsTracker.showSettingsAlert();
        }
        LatLng location = new LatLng(latitude, longitude);
        colgMarker(location, " you", " ");

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        //addMarker(new LatLng(8.189463, 77.401532), "Marker 1", "Description 1");
        //   addMarker(new LatLng(8.188868, 77.408486), "Marker 2", "Description 2");
        //   addCustomMarker(new LatLng(latitude, longitude), "Bus Driver", "Custom Description");
        //originLatLng = new LatLng(latitude, longitude);
        //  LatLng centerPoint = new LatLng(latitude, longitude);
        // checkMarkersWithinRadius(centerPoint, 1000);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String employeeName = studentSnapshot.child("employeeName").getValue(String.class);
                    String empLat = studentSnapshot.child("empLat").getValue(String.class);
                    String empLong = studentSnapshot.child("empLong").getValue(String.class);
                    String empBusNum = studentSnapshot.child("empBusNum").getValue(String.class); // Get employee's bus number

                    Log.d("EmpInfo", "Employee Name: " + employeeName + ", Bus Number: " + empBusNum); // Logging

                    if (employeeName != null && empLat != null && empLong != null && empBusNum != null) {
                        double latitude = Double.parseDouble(empLat);
                        double longitude = Double.parseDouble(empLong);
                        LatLng location = new LatLng(latitude, longitude);
                        //   addMarker(location, employeeName, "");

                        // Check if empBusNum is not null before accessing driver details
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        getLocation();


        // mMap.addMarker(new MarkerOptions().position(originLatLng).title("Origin"));
        //   mMap.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLatLng, 12)); // Zoom level can be adjusted as needed

        // drawRoute(); // Draw the route

        //fetchRoute(originLatLng, destinationLatLng);
    }


    private void fetchRoute(LatLng origin, LatLng destination) {
        // Use Directions API to fetch route
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(Arrays.asList(Place.Field.LAT_LNG));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        placesClient.findCurrentPlace(request).addOnSuccessListener((response) -> {
            List<PlaceLikelihood> placeLikelihoods = response.getPlaceLikelihoods();
            if (placeLikelihoods.size() > 0) {
                LatLng currentLocation = placeLikelihoods.get(0).getPlace().getLatLng();
                String url = getDirectionsUrl(currentLocation, destination);
                new FetchDirectionsTask().execute(url);
            }
        }).addOnFailureListener((exception) -> {
            Toast.makeText(this, "Failed to fetch current location", Toast.LENGTH_SHORT).show();
        });
    }

    private String getDirectionsUrl(LatLng origin, LatLng destination) {
        // Construct URL for Directions API
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destStr = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=" + getString(R.string.google_maps_key);
        return "https://maps.googleapis.com/maps/api/directions/json?" + originStr + "&" + destStr + "&" + sensor + "&" + mode + "&" + key;
    }

    private class FetchDirectionsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Read the input stream into a String
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Parse JSON response and draw polyline on map
            List<LatLng> points = parseDirections(s);
            if (points != null) {
                if (currentPolyline != null)
                    currentPolyline.remove();
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(points)
                        .width(5)
                        .color(getResources().getColor(R.color.red)); // Customize color if needed
                currentPolyline = mMap.addPolyline(polylineOptions);
            }
        }
    }

    private List<LatLng> parseDirections(String jsonData) {
        List<LatLng> points = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray routesArray = jsonObject.getJSONArray("routes");
            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject route = routesArray.getJSONObject(i);
                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                String encodedPolyline = overviewPolyline.getString("points");
                points = PolyUtil.decode(encodedPolyline);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return points;
    }

    private void checkMarkersWithinRadius(LatLng centerPoint, double radius) {
        for (Marker marker : allMarkers) {
            double distance = calculateDistance(centerPoint, marker.getPosition());
            if (distance <= radius) {
                // Marker is within the specified radius
                // You can perform any action or display information about this marker
                Log.d("MarkerWithinRadius", "Marker '" + marker.getTitle() + "' is within the radius.");
               }
        }
    }

    private void drawRoute() {
        // Draw polyline between origin and destination
        List<LatLng> points = new ArrayList<>();
        points.add(originLatLng);
        points.add(destinationLatLng);
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .width(5)
                .color(getResources().getColor(R.color.red)); // Customize color if needed
        mMap.addPolyline(polylineOptions);
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

    }


    private void colgMarker(LatLng position, String title, String snippet) {

      //  BitmapDescriptor customMarkerIcon = fromResource(R.drawable.lo);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet);

        mMap.addMarker(markerOptions);

    }

    private void addCustomMarker(LatLng position, String title, String snippet) {

        BitmapDescriptor customMarkerIcon = fromResource(R.drawable.bus_point);
        Log.i("locationValue", position + "");
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet)
                .icon(customMarkerIcon);

        CircleOptions circleOptions = new CircleOptions()
                .center(position)
                .radius(500)
                .strokeColor(Color.RED)
                .strokeWidth(1)
                .fillColor(Color.parseColor("#7ACD7E78"));


        mMap.addMarker(markerOptions);
        mMap.addCircle(circleOptions);
        float zoomLevel = 15.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoomLevel));

    }
}