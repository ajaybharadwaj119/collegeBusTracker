package com.akinfopark.colgbustracking.loginReg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import com.akinfopark.colgbustracking.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvStd.setOnClickListener(v -> {
            binding.div1.setVisibility(View.VISIBLE);
            binding.div2.setVisibility(View.INVISIBLE);
        });

        binding.tvOp.setOnClickListener(v -> {
            binding.div1.setVisibility(View.INVISIBLE);
            binding.div2.setVisibility(View.VISIBLE);
        });

    }

}