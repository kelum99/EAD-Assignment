package com.example.ead_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ead_mobile.models.RegisterRequestModel;
import com.example.ead_mobile.services.AuthService;
import com.example.ead_mobile.services.ContextService;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button register;
    EditText nic;
    EditText password;
    EditText name;
    EditText email;
    EditText mobile;

    AuthService authService;
    RegisterRequestModel registerRequestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ContextService.getInstance().setApplicationContext(getApplicationContext());
        Objects.requireNonNull(this.getSupportActionBar()).hide();

        authService = AuthService.getInstance();

        this.nic = findViewById(R.id.register_nic);
        this.password = findViewById(R.id.register_password);
        this.name = findViewById(R.id.register_name);
        this.email = findViewById(R.id.register_email);
        this.mobile = findViewById(R.id.register_mobile);
        this.register = findViewById(R.id.register_btn);

        registerRequestModel = new RegisterRequestModel(this.nic.toString(), this.password.toString(), this.mobile.toString(), this.email.toString(), this.name.toString());
        this.register.setOnClickListener(view -> onRegister());
    }

    //calling register method that is implemented in AuthService
    private void onRegister() {
        if (!nic.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty()) {
            authService.register(
                    nic.getText().toString(),
                    password.getText().toString(),
                    name.getText().toString(),
                    email.getText().toString(),
                    mobile.getText().toString(),
                    () -> handleSuccess(),
                    error -> handleFailed(error));
        } else {
            Toast.makeText(this, "All Fields Required!", Toast.LENGTH_LONG).show();
        }
    }

    //on success register
    private void handleSuccess() {
        Toast.makeText(this, "Register Success!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        finish();
    }


    //on failed register
    private void handleFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}