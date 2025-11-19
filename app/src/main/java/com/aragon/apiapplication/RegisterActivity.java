package com.aragon.apiapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.aragon.apiapplication.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etLastName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister, btnLogin;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        setupViewModel();
        setupListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.nameField);
        etLastName = findViewById(R.id.lastNameField);
        etEmail = findViewById(R.id.emailField);
        etPassword = findViewById(R.id.registerPasswordField);
        etConfirmPassword = findViewById(R.id.registerConfirmPasswordField);
        btnRegister = findViewById(R.id.registerBttn2);
        btnLogin = findViewById(R.id.loginBttn);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        viewModel.getRegisterResult().observe(this, registerResponse -> {
            if (registerResponse != null && registerResponse.isSuccess()) {
                Toast.makeText(this, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (validateInputs(name, lastName, email, password, confirmPassword)) {
                viewModel.register(name, lastName, email, password, confirmPassword);
            }
        });

        btnLogin.setOnClickListener(v -> finish());
    }

    private boolean validateInputs(String name, String lastName, String email,
                                   String password, String confirmPassword) {
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}