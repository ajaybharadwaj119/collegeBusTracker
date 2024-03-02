package com.akinfopark.colgbustracking.loginReg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.akinfopark.colgbustracking.EmployeeInfo;
import com.akinfopark.colgbustracking.Utils.GpsTracker;
import com.akinfopark.colgbustracking.databinding.ActivityStudentRegBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class StudentRegActivity extends AppCompatActivity {
    ActivityStudentRegBinding binding;
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private GpsTracker gpsTracker;
    List<String> drivBusNumbers = new ArrayList<>();
    String email, password, name, lat = "", longe = "", busNum="", type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.btnregister.setOnClickListener(v -> {
            registerNewUser();
        });


        databaseReference= FirebaseDatabase.getInstance().getReference().child("driver");


        // Listener to retrieve data
        ValueEventListener driverListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    String drivBusNumber = driverSnapshot.child("drivBusNumber").getValue(String.class);
                    if (drivBusNumber != null) {
                        drivBusNumbers.add(drivBusNumber);
                    }
                }


                setSpinner(drivBusNumbers);
                // Here you have the list of drivBusNumbers, you can use it as needed
                for (String busNumber : drivBusNumbers) {
                    Log.d("MainActivity", "Bus Number: " + busNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("MainActivity", "loadPost:onCancelled", databaseError.toException());
            }
        };

        // Attach listener to database reference
        databaseReference.addListenerForSingleValueEvent(driverListener);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void registerNewUser() {

        // show the visibility of progress bar to show loading
        binding.progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings

        email = binding.email.getText().toString();
        name = binding.tvName.getText().toString();
       // busNum = binding.tvBusNum.getText().toString();

        password = binding.passwd.getText().toString();

        gpsTracker = new GpsTracker(getApplicationContext());
        if (gpsTracker.canGetLocation()) {
            lat = String.valueOf(gpsTracker.getLatitude());
            longe = String.valueOf(gpsTracker.getLongitude());
          /*  tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));*/
        } else {
            gpsTracker.showSettingsAlert();
        }

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // create new user or register new user
        String finalLonge = longe;
        String finalLat = lat;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                            "Registration successful!",
                                            Toast.LENGTH_LONG)
                                    .show();
                            // hide the progress bar
                            binding.progressbar.setVisibility(View.GONE);
                            // if the user created intent to login activity

                            addDatatoFirebase(name, email, finalLat, finalLonge, "", busNum);
                            Intent intent
                                    = new Intent(StudentRegActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // Registration failed
                            Toast.makeText(
                                            getApplicationContext(),
                                            "Registration failed!!"
                                                    + " Please try again later",
                                            Toast.LENGTH_LONG)
                                    .show();
                            // hide the progress bar
                            binding.progressbar.setVisibility(View.GONE);
                        }
                    }
                });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the spinner
                busNum= parent.getItemAtPosition(position).toString();
                // Do whatever you want with the selectedBusNumber
                Toast.makeText(StudentRegActivity.this, "Selected Bus Number: " + busNum, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    private void setSpinner(List<String> drivBusNumbers) {


        // Create ArrayAdapter using the list of drivBusNumbers
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, drivBusNumbers);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinner.setAdapter(adapter);
    }

    private void addDatatoFirebase(String name, String email, String lat, String longe, String type, String busNum) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("student").child(name);
        EmployeeInfo employeeInfo = new EmployeeInfo(); // Create a new instance of EmployeeInfo

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.i("", "" + token);
                        employeeInfo.setEmpFcm(""+token);
                    }
                });

        // Set the values using setter methods
        employeeInfo.setEmployeeName(name);
        employeeInfo.setEmployeeAddress(email);
        employeeInfo.setEmployeeContactNumber(lat);
        employeeInfo.setEmpLat(lat);
        employeeInfo.setEmpLong(longe);
        employeeInfo.setEmpEmail(email);
        employeeInfo.setEmpType(type);
        employeeInfo.setEmpBusNum(busNum);


        // Assuming databaseReference is properly initialized elsewhere in your code
        databaseReference.setValue(employeeInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully added
                        Toast.makeText(getApplicationContext(), "Data added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add data
                        Toast.makeText(getApplicationContext(), "Fail to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}