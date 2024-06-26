package com.akinfopark.colgbustracking.loginReg;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import com.akinfopark.colgbustracking.API.UserData;
import com.akinfopark.colgbustracking.AdminActivity;
import com.akinfopark.colgbustracking.EmployeeInfo;
import com.akinfopark.colgbustracking.MainActivity;
import com.akinfopark.colgbustracking.StudentActivity;
import com.akinfopark.colgbustracking.Utils.DialogUtils;
import com.akinfopark.colgbustracking.Utils.MyPrefs;
import com.akinfopark.colgbustracking.databinding.ActivityLoginBinding;
import com.akinfopark.colgbustracking.databinding.DialogAdminLoginBinding;
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


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;

    DialogAdminLoginBinding adminLoginBinding;
    AlertDialog dialog;
    String token = "", name = "", number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        adminLoginBinding = DialogAdminLoginBinding.inflate(getLayoutInflater());
        dialog = DialogUtils.getCustomAlertDialog(this, adminLoginBinding.getRoot());

        requestLocationPermission();

        binding.tvRegStd.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, StudentRegActivity.class);
            startActivity(intent);
        });

        binding.tvRegDriver.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DriverRegActivity.class);
            startActivity(intent);
        });


        binding.imgDrivSel.setOnClickListener(v -> {
            binding.imgDrivSel.setVisibility(View.GONE);
            binding.imgStdSel.setVisibility(View.VISIBLE);
            binding.tvLogin.setVisibility(View.VISIBLE);
            binding.tvLoginDriv.setVisibility(View.GONE);
            binding.tvRegStd.setVisibility(View.VISIBLE);
            binding.tvRegDriver.setVisibility(View.GONE);
        });

        binding.imgStdSel.setOnClickListener(v -> {
            binding.imgDrivSel.setVisibility(View.VISIBLE);
            binding.imgStdSel.setVisibility(View.GONE);

            binding.tvLogin.setVisibility(View.GONE);
            binding.tvLoginDriv.setVisibility(View.VISIBLE);
            binding.tvRegStd.setVisibility(View.GONE);
            binding.tvRegDriver.setVisibility(View.VISIBLE);
        });

      /*  binding.tvTest.setOnClickListener(v -> {
          //  testFun();
            //Toast.makeText(this, "Ajay Test", Toast.LENGTH_SHORT).show();
           // searchEmail("test123@gmail.com");
            try {
                NotificationManager.callNotifAPI();
            } catch (JSONException e) {

            }
            //  sendNotification(token,"Test","Test body");
        });*/

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

        binding.tvLoginDriv.setOnClickListener(v -> {
            loginUserDrivAccount();
        });
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Navigate to the desired location in the database
        DatabaseReference settingsRef = databaseRef.child("settings");


        // Attach a listener to read the data at the desired location
        settingsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Retrieve the data snapshot
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Retrieve the colgLat value
                  /*  String colgLat = dataSnapshot.child("colgLat").getValue(String.class);
                    String colgLon = dataSnapshot.child("colgLong").getValue(String.class);*/
                   /* MyPrefs.getInstance(getApplicationContext()).putString(UserData.KEY_COLG_LAT, colgLat);
                    MyPrefs.getInstance(getApplicationContext()).putString(UserData.KEY_COLG_LAT, colgLon);*/

                    // You can use the colgLat value here
                } else {
                    Log.d("TAG", "No such document");
                }
            } else {
                Log.d("TAG", "get failed with ", task.getException());
            }
        });

        binding.tvAdmin.setOnClickListener(v -> {
          /*  Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);*/

            dialog.show();
        });

        adminLoginBinding.tvLogin.setOnClickListener(v -> {
            AdName = "admin";
            AdPassword = "admin@123";

            if (!adminLoginBinding.edtName.getText().toString().equalsIgnoreCase(AdName)){
                Toast.makeText(this, "Invalid Admin user name or password", Toast.LENGTH_SHORT).show();
            } else if (!adminLoginBinding.edtpassword.getText().toString().equalsIgnoreCase(AdPassword)) {
                Toast.makeText(this, "Invalid Admin user name or password", Toast.LENGTH_SHORT).show();
            }else {
                MyPrefs.getInstance(getApplicationContext()).putString("login","admin");
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
            }

        });

    }

    String AdName = "", AdPassword = "";

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

                                    MyPrefs.getInstance(getApplicationContext()).putString("login", "driver");

                                    // hide the progress bar

                                    // if sign-in is successful
                                    // intent to home activity
                                    MyPrefs.getInstance(getApplicationContext()).putString("DrivEmail", email);
                                    Bundle bundle = new Bundle();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    bundle.putString("email", email);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    finish();
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

                                    MyPrefs.getInstance(getApplicationContext()).putString("login", "student");
                                    // hide the progress bar
                                    MyPrefs.getInstance(getApplicationContext()).putString("email", email);
                                    // if sign-in is successful
                                    // intent to home activity
                                    Bundle bundle = new Bundle();
                                    Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                                    //bundle.putString("email", email);

                                    intent.putExtras(bundle);
                                    startActivity(intent);

                                    finish();
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