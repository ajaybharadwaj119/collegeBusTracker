package com.akinfopark.colgbustracking.loginReg;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

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
import android.widget.Toast;

import com.akinfopark.colgbustracking.EmployeeInfo;
import com.akinfopark.colgbustracking.MainActivity;
import com.akinfopark.colgbustracking.Utils.MyPrefs;
import com.akinfopark.colgbustracking.databinding.ActivityLoginBinding;
import com.akinfopark.colgbustracking.firebase.NotificationManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;
    String token="",name="",number="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        requestLocationPermission();

        binding.tvRegStd.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, StudentRegActivity.class);
            startActivity(intent);
        });

        binding.tvRegDriver.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DriverRegActivity.class);
            startActivity(intent);
        });

        binding.tvLoginDriv.setOnClickListener(v->{

        });

        binding.tvTest.setOnClickListener(v -> {
          //  testFun();
            //Toast.makeText(this, "Ajay Test", Toast.LENGTH_SHORT).show();
           // searchEmail("test123@gmail.com");
            try {
                NotificationManager.callNotifAPI();
            } catch (JSONException e) {

            }
            //  sendNotification(token,"Test","Test body");
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        Log.i("fcm Token ", "" + token);
                    }
                });

        binding.tvLogin.setOnClickListener(v -> {
            loginUserAccount();
        });

        binding.tvLoginDriv.setOnClickListener(v->{
            loginUserDrivAccount();
        });

    }

    public void searchEmail(String email) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("student");

        Query query = mDatabase.orderByChild("empEmail").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Get the object that contains the email
                        // Here you can perform any necessary actions with the found data
                        // For example, printing the email address
                        String empEmail = snapshot.child("empEmail").getValue(String.class);
                        String empName = snapshot.child("employeeName").getValue(String.class);
                        Toast.makeText(LoginActivity.this, ""+empName, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.out.println("Email not found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private static final int REQUEST_LOCATION_PERMISSION = 1; // Request code


    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Permission already granted, proceed with location access
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(); // Permission granted, access location
            } else {
                // Handle permission denied case (e.g., show explanation)
            }
        }
    }


    void testFun() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference employeeInfoRef = firebaseDatabase.getReference("EmployeeInfo");

// Attach a ValueEventListener to the reference
        employeeInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method will be called once with the initial data from the database.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Loop through each child node under the reference
                    // Here, snapshot will represent each child node
                    // You can access the data within each child node using getValue() method
                    EmployeeInfo employeeInfo = snapshot.getValue(EmployeeInfo.class);

                    // Now you can access the properties of the EmployeeInfo object
                    String empName = employeeInfo.getEmployeeName();
                    String empEmail = employeeInfo.getEmpEmail();
                    String empLat = employeeInfo.getEmpLat();
                    String empLong = employeeInfo.getEmpLong();
                    String empBusNum = employeeInfo.getEmpBusNum();
                    String empType = employeeInfo.getEmpType();

                    // Do something with the retrieved data
                    // For example, you can log it or display it in your app
                    Log.d("EmployeeInfo", "Name: " + empName + ", Email: "
                            + empEmail + ", Latitude: " + empLat + ", Longitude: "
                            + empLong + ", Bus Number: " + empBusNum + ", Type: " + empType);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("Firebase", "Error reading data: " + databaseError.getMessage());
            }
        });

    }

    private void loginUserDrivAccount() {

        // Take the value of two edit texts in Strings
        String email, password;
        email = binding.edtEmail.getText().toString();
        password = binding.edtPass.getText().toString();

        // validations for input email and password
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

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    MyPrefs.getInstance(getApplicationContext()).putString("login","driver");

                                    // hide the progress bar

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                } else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
    }

    private void loginUserAccount() {

        // Take the value of two edit texts in Strings
        String email, password;
        email = binding.edtEmail.getText().toString();
        password = binding.edtPass.getText().toString();

        // validations for input email and password
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

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    MyPrefs.getInstance(getApplicationContext()).putString("login","student");
                                    // hide the progress bar

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                } else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
    }
}