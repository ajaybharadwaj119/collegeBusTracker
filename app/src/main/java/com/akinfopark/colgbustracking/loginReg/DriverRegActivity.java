package com.akinfopark.colgbustracking.loginReg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.akinfopark.colgbustracking.DriverInfo;
import com.akinfopark.colgbustracking.EmployeeInfo;
import com.akinfopark.colgbustracking.R;
import com.akinfopark.colgbustracking.databinding.ActivityDriverRegBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.Driver;

public class DriverRegActivity extends AppCompatActivity {
    ActivityDriverRegBinding binding;
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String token = "";
    String drivLat = "", drivLong = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        Log.i("", "" + token);

                    }
                });

        binding.btnregister.setOnClickListener(v -> {
            regDrive();
        });

    }

    void regDrive() {

        String name = binding.tvName.getText().toString();
        String email = binding.email.getText().toString();
        // String number = binding.number.getText().toString();
        String password = binding.passwd.getText().toString();
        String busNum = binding.tvBusNum.getText().toString();


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

                            addDataToFirebase(name, email, busNum, password);
                            Intent intent
                                    = new Intent(DriverRegActivity.this,
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

    }

    private void addDataToFirebase(String name, String email, String busNum, String password) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("driver").child(name);

        DriverInfo driverInfo = new DriverInfo();



        // Set the values using setter methods
        driverInfo.setDrivLat(drivLat);
        driverInfo.setDrivLong(drivLong);
        driverInfo.setDrivName(name);
        driverInfo.setDrivBusNumber(busNum);
        driverInfo.setDrivNumber(email);
        driverInfo.setDriveFToken(token);
        driverInfo.setDrivPassword(password);


        // Assuming databaseReference is properly initialized elsewhere in your code
        databaseReference.setValue(driverInfo)
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