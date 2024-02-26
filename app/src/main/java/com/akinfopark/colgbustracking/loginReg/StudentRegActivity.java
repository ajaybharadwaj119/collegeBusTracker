package com.akinfopark.colgbustracking.loginReg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.akinfopark.colgbustracking.EmployeeInfo;
import com.akinfopark.colgbustracking.MainActivity;
import com.akinfopark.colgbustracking.R;
import com.akinfopark.colgbustracking.Utils.GpsTracker;
import com.akinfopark.colgbustracking.databinding.ActivityStudentRegBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class StudentRegActivity extends AppCompatActivity {
    ActivityStudentRegBinding binding;
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EmployeeInfo employeeInfo;
    private GpsTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentRegBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.btnregister.setOnClickListener(v -> {
            registerNewUser();
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("EmployeeInfo");
        employeeInfo = new EmployeeInfo();

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
        String email, password,name,lat = "",longe="",busNum,type;
        email = binding.email.getText().toString();
        name= binding.tvName.getText().toString();
        busNum = binding.tvBusNum.getText().toString();

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

                            addDatatoFirebase(name,email, finalLat, finalLonge,"",busNum);
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
    }

    private void addDatatoFirebase(String name, String email, String lat,String longe,String type,String busNum) {
        // below 3 lines of code is used to set
        // data in our object class.
        employeeInfo.setEmpName(name);
        employeeInfo.setEmpEmail(email);
        employeeInfo.setEmpLat(lat);
        employeeInfo.setEmpLong(longe);
        employeeInfo.setEmpType(type);
        employeeInfo.setEmpBusNum(busNum);

        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(employeeInfo);

                // after adding this data we are showing toast message.
                Toast.makeText(getApplicationContext(), "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(getApplicationContext(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}