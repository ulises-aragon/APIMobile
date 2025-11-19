package com.aragon.apiapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.aragon.apiapplication.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupViewModel();
        setupListeners();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.emailField);
        etPassword = findViewById(R.id.passwordField);
        btnLogin = findViewById(R.id.loginBttn2);
        btnRegister = findViewById(R.id.registerBttn);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getLoginResult().observe(this, loginResponse -> {
            if (loginResponse != null) {
                Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ProductsActivity.class));
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, password);
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}